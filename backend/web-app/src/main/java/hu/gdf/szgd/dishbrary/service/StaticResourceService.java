package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.StaticResourceComponentType;
import hu.gdf.szgd.dishbrary.db.repository.RecipeRepository;
import hu.gdf.szgd.dishbrary.security.SecurityUtils;
import hu.gdf.szgd.dishbrary.service.exception.ConfigurationErrorException;
import hu.gdf.szgd.dishbrary.service.exception.ResourceCannotBeSavedException;
import hu.gdf.szgd.dishbrary.service.exception.ResourceNotFoundException;
import hu.gdf.szgd.dishbrary.web.model.FileResource;
import hu.gdf.szgd.dishbrary.web.model.RecipeRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
@ConfigurationProperties(prefix = "dishbrary.resources")
public class StaticResourceService {

	private Map<String, String> imageBasePathByComponentName = new HashMap<>();

	@Autowired
	private RecipeRepository recipeRepository;
	@Autowired
	private RecipeService recipeService;

	public File getImageForComponentByName(StaticResourceComponentType componentType, String imgName) throws ResourceNotFoundException {
		log.debug("Image requested for component: {} with name: {}", componentType.name(), imgName);

		String basePathForComponent = getBasePathForComponentType(componentType);

		File image = new File(basePathForComponent, imgName);

		if (log.isDebugEnabled()) {
			log.debug("Image requested from the following location: {}", image.getAbsolutePath());
		}

		if (!image.exists()) {
			throw new ResourceNotFoundException("Cannot find resource: " + image.getAbsolutePath());
		}

		return image;
	}

	public void uploadRecipeVideo(Long recipeId, FileResource videoResource) {
		RecipeRestModel recipe = recipeService.findRecipeById(recipeId);

		String basePathToSaveResource = getBasePathForComponentType(StaticResourceComponentType.RECIPE)
				+ getRemainingPathForRecipeByComponentSubType(recipeId, StaticResourceComponentType.StaticResourceComponentSubType.VIDEO);

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
	}

	public void uploadRecipeImages(Long recipeId, String selectedCoverImageFileName, List<FileResource> fileResources) {

		RecipeRestModel recipe = recipeService.findRecipeById(recipeId);

		String basePathToSaveResource = getBasePathForComponentType(StaticResourceComponentType.RECIPE)
				+ getRemainingPathForRecipeByComponentSubType(recipeId, StaticResourceComponentType.StaticResourceComponentSubType.IMAGE);

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

	private void removeOldImagesFromDirectory(String dirName, RecipeRestModel recipeWithNewFiles) {
		try (Stream<Path> walk = Files.walk(Paths.get(dirName))) {

			List<File> result = walk.filter(path -> {
				String fileName = path.getFileName().toString();

				return Files.isRegularFile(path) &&
						!recipeWithNewFiles.getCoverImageFileName().equals(fileName) &&
						!recipeWithNewFiles.getAdditionalImagesFileNames().contains(fileName);
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

	private String getBasePathForComponentType(StaticResourceComponentType componentType) {
		String basePathForComponent = imageBasePathByComponentName.get(componentType.name().toLowerCase());

		if (StringUtils.isEmpty(basePathForComponent)) {
			String errorStr = "Cannot find image base path configuration for the following component: "
					+ componentType.name().toLowerCase()
					+ ". Expected configuration is most likely missing: [dishbrary.images.basePath." + componentType.name().toLowerCase() + "]";

			log.error(errorStr);
			throw new ConfigurationErrorException(errorStr);
		}

		return basePathForComponent;
	}

	private String getRemainingPathForRecipeByComponentSubType(Long recipeId, StaticResourceComponentType.StaticResourceComponentSubType componentSubType) {
		return SecurityUtils.getDishbraryUserFromContext().getId() +
				"/" + StaticResourceComponentType.RECIPE.name().toLowerCase() +
				"/" + recipeId +
				"/" + componentSubType.name().toLowerCase();
	}

	public Map<String, String> getImageBasePathByComponentName() {
		return imageBasePathByComponentName;
	}
}
