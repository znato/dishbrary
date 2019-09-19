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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public void uploadRecipeAdditionalImages(Long recipeId, List<FileResource> fileResources) {

		RecipeRestModel recipe = recipeService.findRecipeById(recipeId);

		String basePathToSaveResource = getBasePathForComponentType(StaticResourceComponentType.RECIPE)
				+ getRemainingPathForRecipeByComponentSubType(recipeId, StaticResourceComponentType.StaticResourceComponentSubType.IMAGE);

		log.debug("Image upload base path for recipe with id: {} is {}", recipe, basePathToSaveResource);

		File directoryToSaveIn = new File(basePathToSaveResource);

		if (!directoryToSaveIn.exists()) {
			log.info("Image directory does not exist for recipe with id: {}! Creating directory: {}", recipeId, basePathToSaveResource);
			directoryToSaveIn.mkdirs();
		}

		List<String> recipeAdditionalimageFilenames = new ArrayList<>(fileResources.size());

		for (FileResource fileResource : fileResources) {
			InputStream inputStream = fileResource.getInputStream();

			try (OutputStream out = new FileOutputStream(new File(directoryToSaveIn, fileResource.getFileName()))) {
				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = inputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}

				log.info("The following image is saved for recipe with id: {}, image name: {}", recipeId, basePathToSaveResource + fileResource.getFileName());

				recipeAdditionalimageFilenames.add(fileResource.getFileName());
			} catch (IOException ex) {
				//clean up all files if some cannot be saved

				log.error("An exception occurred while saving image(s) for recipe with id: {}! Cleaning up saved images if there was any", recipeId, ex);

				for (FileResource fr : fileResources) {
					File file = new File(directoryToSaveIn, fr.getFileName());
					if (file.exists()) {
						log.debug("Cleaning up image due to IOException for recipe with id: {}, image: {}", recipeId, basePathToSaveResource + fr.getFileName());
						file.delete();
					} else {
						break;
					}
				}

				throw new ResourceCannotBeSavedException("A kiválasztott fájl(ok) mentése közben hiba lépett fel. Kérlek próbald újra!");
			}
		}

		recipe.setAdditionalImagesFileNames(recipeAdditionalimageFilenames);

		recipeService.saveAdditionalImagesToRecipe(recipe);
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
