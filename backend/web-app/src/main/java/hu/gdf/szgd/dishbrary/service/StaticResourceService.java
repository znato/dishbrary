package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.StaticResourceComponentType;
import hu.gdf.szgd.dishbrary.service.exception.ConfigurationErrorException;
import hu.gdf.szgd.dishbrary.service.exception.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@ConfigurationProperties(prefix = "dishbrary.resources")
public class StaticResourceService {

	private Map<String, String> imageBasePathByComponentName = new HashMap<>();

	public File getImageForComponentByName(StaticResourceComponentType componentType, String imgName) throws ResourceNotFoundException {
		log.debug("Image requested for component: {} with name: {}",  componentType.name(), imgName);

		String basePathForComponent = imageBasePathByComponentName.get(componentType.name().toLowerCase());

		if (StringUtils.isEmpty(basePathForComponent)) {
			String errorStr = "Cannot find image base path configuration for the following component: "
					+ componentType.name().toLowerCase()
					+ ". Expected configuration is most likely missing: [dishbrary.images.basePath." + componentType.name().toLowerCase() + "]";

			log.error(errorStr);
			throw new ConfigurationErrorException(errorStr);
		}

		File image = new File(basePathForComponent, imgName);

		if (log.isDebugEnabled()) {
			log.debug("Image requested from the following location: {}", image.getAbsolutePath());
		}

		if (!image.exists()) {
			throw new ResourceNotFoundException("Cannot find resource: " + image.getAbsolutePath());
		}

		return image;
	}

	public Map<String, String> getImageBasePathByComponentName() {
		return imageBasePathByComponentName;
	}
}
