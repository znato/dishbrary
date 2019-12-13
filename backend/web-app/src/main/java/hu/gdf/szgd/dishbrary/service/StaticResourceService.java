package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.StaticResourceComponentType;
import hu.gdf.szgd.dishbrary.StaticResourceComponentType.StaticResourceComponentSubType;
import hu.gdf.szgd.dishbrary.db.repository.UserRepository;
import hu.gdf.szgd.dishbrary.security.SecurityUtils;
import hu.gdf.szgd.dishbrary.service.exception.ResourceCannotBeSavedException;
import hu.gdf.szgd.dishbrary.service.exception.ResourceNotFoundException;
import hu.gdf.szgd.dishbrary.web.model.FileResource;
import hu.gdf.szgd.dishbrary.web.model.RecipeRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class StaticResourceService {

	@Value("${dishbrary.recipe.images.defaultCoverImage.name}")
	private String defaultRecipeCoverImageFileName;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RecipeService recipeService;
	@Autowired
	private ResourcePathService resourcePathService;

	public File getImageForComponentByName(StaticResourceComponentType componentType, String imgName) throws ResourceNotFoundException {
		log.debug("Image requested for component: {} with name: {}", componentType.name(), imgName);

		String basePathForComponent = resourcePathService.getBasePathForComponentType(componentType);

		File image = new File(basePathForComponent, imgName);

		if (log.isDebugEnabled()) {
			log.debug("Image requested from the following location: {}", image.getAbsolutePath());
		}

		if (!image.exists()) {
			throw new ResourceNotFoundException("Cannot find resource: " + image.getAbsolutePath());
		}

		return image;
	}

	public File getResourceForRecipeByName(Long recipeId, String resourceName, StaticResourceComponentSubType resourceType) throws ResourceNotFoundException {
		String basePathForComponent = resourcePathService.getBasePathForComponentType(StaticResourceComponentType.RECIPE);

		//recipe images and videos are stored separately in dedicated folders identified by the recipe owner and recipe id
		//image upload for recipe is fully optional, in case user did not upload any image for the recipe a default will be shown
		//the default image located in a general place so get the images full path for the recipe only if not the default is set
		if (StaticResourceComponentSubType.VIDEO.equals(resourceType) || !defaultRecipeCoverImageFileName.equals(resourceName)) {
			Long ownerId = userRepository.findUserIdByRecipesId(recipeId);

			basePathForComponent += resourcePathService.getRemainingPathForRecipeByComponentSubType(ownerId, recipeId, resourceType);
		}

		File resource = new File(basePathForComponent, resourceName);

		if (log.isDebugEnabled()) {
			log.debug("Resource requested from the following location: {}", resource.getAbsolutePath());
		}

		if (!resource.exists()) {
			throw new ResourceNotFoundException("Cannot find resource: " + resource.getAbsolutePath());
		}

		return resource;
	}

	public void uploadRecipeVideo(Long recipeId, FileResource videoResource) {
		RecipeRestModel recipe = recipeService.findRecipeById(recipeId);

		String basePathToSaveResource = resourcePathService.getFullResourceDirectoryPathForRecipeByComponentType(SecurityUtils.getDishbraryUserFromContext().getId(), recipeId,
						StaticResourceComponentType.RECIPE, StaticResourceComponentSubType.VIDEO);

		log.debug("Video upload base path for recipe with id: {} is {}", recipe, basePathToSaveResource);

		File directoryToSaveIn = new File(basePathToSaveResource);

		if (!directoryToSaveIn.exists()) {
			log.info("Video directory does not exist for recipe with id: {}! Creating directory: {}", recipeId, basePathToSaveResource);
			directoryToSaveIn.mkdirs();
		}

		InputStream inputStream = videoResource.getInputStream();

		try (OutputStream out = new FileOutputStream(new File(directoryToSaveIn, videoResource.getFileName()))) {
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			recipe.setVideoFileName(videoResource.getFileName());

			log.info("The following video is saved for recipe with id: {}, video name: {}", recipeId, basePathToSaveResource + videoResource.getFileName());
		} catch (Exception ex) {
			log.error("An exception occurred while saving video for recipe with id: {}! Cleaning up saved video", recipeId, ex);

			throw new ResourceCannotBeSavedException("A kiválasztott videó fájl mentése közben hiba lépett fel. Kérlek próbald újra!");
		}

		recipeService.saveVideoToRecipe(recipe);

		removeOldVideoFromDirectory(basePathToSaveResource, recipe);
	}

	public void deleteRecipeVideo(Long recipeId) {
		RecipeRestModel recipe = recipeService.findRecipeById(recipeId);

		recipe.setVideoFileName(null);

		recipeService.saveVideoToRecipe(recipe);

		String recipeVideoDirectory = resourcePathService.getFullResourceDirectoryPathForRecipeByComponentType(SecurityUtils.getDishbraryUserFromContext().getId(), recipeId,
				StaticResourceComponentType.RECIPE, StaticResourceComponentSubType.VIDEO);;

		removeOldVideoFromDirectory(recipeVideoDirectory, recipe);
	}

	public void deleteAllRecipeImages(Long recipeId) {
		RecipeRestModel recipe = recipeService.findRecipeById(recipeId);

		if (CollectionUtils.isEmpty(recipe.getAdditionalImagesFileNames()) && StringUtils.isEmpty(recipe.getCoverImageFileName())) {
			return;
		}

		recipe.setCoverImageFileName(null);
		recipe.setAdditionalImagesFileNames(null);

		recipeService.saveImagesToRecipe(recipe);

		String basePathToImages = resourcePathService.getFullResourceDirectoryPathForRecipeByComponentType(SecurityUtils.getDishbraryUserFromContext().getId(), recipeId,
				StaticResourceComponentType.RECIPE, StaticResourceComponentSubType.IMAGE);

		removeOldImagesFromDirectory(basePathToImages, recipe);
	}

	public void uploadRecipeImages(Long recipeId, String selectedCoverImageFileName, List<FileResource> fileResources) {

		RecipeRestModel recipe = recipeService.findRecipeById(recipeId);

		String basePathToSaveResource = resourcePathService.getFullResourceDirectoryPathForRecipeByComponentType(SecurityUtils.getDishbraryUserFromContext().getId(), recipeId,
				StaticResourceComponentType.RECIPE, StaticResourceComponentSubType.IMAGE);;

		log.debug("Image upload base path for recipe with id: {} is {}", recipe, basePathToSaveResource);

		File directoryToSaveIn = new File(basePathToSaveResource);

		if (!directoryToSaveIn.exists()) {
			log.info("Image directory does not exist for recipe with id: {}! Creating directory: {}", recipeId, basePathToSaveResource);
			directoryToSaveIn.mkdirs();
		}

		List<String> recipeAdditionalImageFilenames = new ArrayList<>(fileResources.size());

		for (FileResource fileResource : fileResources) {
			InputStream inputStream = fileResource.getInputStream();

			try (OutputStream out = new FileOutputStream(new File(directoryToSaveIn, fileResource.getFileName()))) {
				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = inputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}

				log.info("The following image is saved for recipe with id: {}, image name: {}", recipeId, basePathToSaveResource + fileResource.getFileName());

				if (fileResource.getFileName().equals(selectedCoverImageFileName)) {
					log.info("The following cover image is selected for recipe with id: {}, image name: {}", recipeId, selectedCoverImageFileName);

					recipe.setCoverImageFileName(fileResource.getFileName());
				} else {
					recipeAdditionalImageFilenames.add(fileResource.getFileName());
				}
			} catch (Exception ex) {
				//clean up all files if some cannot be saved

				log.error("An exception occurred while saving image(s) for recipe with id: {}! Cleaning up saved images if there was any", recipeId, ex);

				for (FileResource fr : fileResources) {
					File file = new File(directoryToSaveIn, fr.getFileName());
					if (file.exists()) {
						log.debug("Cleaning up image due to an exception for recipe with id: {}, image: {}", recipeId, basePathToSaveResource + fr.getFileName());
						file.delete();
					} else {
						break;
					}
				}

				throw new ResourceCannotBeSavedException("A kiválasztott fájl(ok) mentése közben hiba lépett fel. Kérlek próbald újra!");
			}
		}

		recipe.setAdditionalImagesFileNames(recipeAdditionalImageFilenames);

		recipeService.saveImagesToRecipe(recipe);

		removeOldImagesFromDirectory(basePathToSaveResource, recipe);
	}

	private void removeOldVideoFromDirectory(String dirName,  RecipeRestModel recipeWithNewVideo) {
		try (Stream<Path> walk = Files.walk(Paths.get(dirName))) {

			List<File> result = walk.filter(path -> {
				String fileName = path.getFileName().toString();

				return Files.isRegularFile(path) &&
						!fileName.equals(recipeWithNewVideo.getVideoFileName());
			}).map(x -> x.toFile()).collect(Collectors.toList());

			result.forEach(file -> {
				if (log.isDebugEnabled()) {
					log.debug("Deleting old file for recipe! RecipeId: {}, fileName: {}", recipeWithNewVideo.getId(), file.getAbsolutePath());
				}
				file.delete();
			});

		} catch (IOException e) {
			log.error("Could not delete old video file for recipe with id: {}", recipeWithNewVideo.getId(), e);
		}
	}

	private void removeOldImagesFromDirectory(String dirName, RecipeRestModel recipeWithNewFiles) {
		boolean coverImageRemoved = StringUtils.isEmpty(recipeWithNewFiles.getCoverImageFileName());
		boolean additionalImagesRemoved = CollectionUtils.isEmpty(recipeWithNewFiles.getAdditionalImagesFileNames());

		try (Stream<Path> walk = Files.walk(Paths.get(dirName))) {

			List<File> result = walk.filter(path -> {
				String fileName = path.getFileName().toString();

				return Files.isRegularFile(path) &&
						(coverImageRemoved || !recipeWithNewFiles.getCoverImageFileName().equals(fileName)) &&
						(additionalImagesRemoved || !recipeWithNewFiles.getAdditionalImagesFileNames().contains(fileName));
			}).map(x -> x.toFile()).collect(Collectors.toList());

			result.forEach(file -> {
				if (log.isDebugEnabled()) {
					log.debug("Deleting old file for recipe! RecipeId: {}, fileName: {}", recipeWithNewFiles.getId(), file.getAbsolutePath());
				}
				file.delete();
			});

		} catch (IOException e) {
			log.error("Could not delete old image files for recipe with id: {}", recipeWithNewFiles.getId(), e);
		}
	}
}
