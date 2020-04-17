package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.StaticResourceComponentType;
import hu.gdf.szgd.dishbrary.StaticResourceComponentType.StaticResourceComponentSubType;
import hu.gdf.szgd.dishbrary.service.exception.ConfigurationErrorException;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static java.io.File.separator;

@Service
@Log4j2
@ConfigurationProperties(prefix = "dishbrary.resources")
public class ResourcePathService {
	private static final String PROFILE_IMAGE_DIR_WITH_SEPARATOR = separator +"profileImg" + separator;

	private Map<String, String> imageBasePathByComponentName = new HashMap<>();

	public String getBasePathForComponentType(StaticResourceComponentType componentType) {
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

	public String getRemainingPathForRecipeBaseDirectory(Long recipeOwnerId, Long recipeId) {
		return recipeOwnerId +
				separator + StaticResourceComponentType.RECIPE.name().toLowerCase() +
				separator + recipeId;
	}

	public String getRemainingPathForRecipeByComponentSubType(Long recipeOwnerId, Long recipeId, StaticResourceComponentSubType componentSubType) {
		return getRemainingPathForRecipeBaseDirectory(recipeOwnerId, recipeId) +
				separator + componentSubType.name().toLowerCase();
	}

	public String getFullResourceDirectoryRootPathForRecipe(Long recipeOwnerId, Long recipeId) {
		return getBasePathForComponentType(StaticResourceComponentType.RECIPE) + getRemainingPathForRecipeBaseDirectory(recipeOwnerId, recipeId);
	}

	public String getFullResourceDirectoryPathForRecipeByComponentType(Long recipeOwnerId, Long recipeId, StaticResourceComponentType componentType, StaticResourceComponentSubType componentSubType) {
		return getBasePathForComponentType(componentType) + getRemainingPathForRecipeByComponentSubType(recipeOwnerId, recipeId, componentSubType);
	}

	public String getFullResourceDirectoryPathForUserProfileImage(Long userId) {
		return getBasePathForComponentType(StaticResourceComponentType.USER) + separator + userId + PROFILE_IMAGE_DIR_WITH_SEPARATOR;
	}

	public Map<String, String> getImageBasePathByComponentName() {
		return imageBasePathByComponentName;
	}
}
