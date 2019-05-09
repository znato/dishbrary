package hu.gdf.szgd.dishbrary.transformer;

import hu.gdf.szgd.dishbrary.StaticResourceComponentType;
import hu.gdf.szgd.dishbrary.db.entity.Cuisine;
import hu.gdf.szgd.dishbrary.web.model.CuisineRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class CuisineTransformer {

	@Value("${dishbrary.cuisine.images.default.name}")
	private String defaultCuisineIconName;

	public CuisineRestModel transform(Cuisine cuisine) {
		log.debug("Transform Cuisine with name:{} and id: {}", cuisine.getName(), cuisine.getId());

		StringBuilder iconUrl = new StringBuilder(StaticResourceComponentType.CUISINE.name()).append("/");

		String imgFileName = cuisine.getIconFileName();
		if (!StringUtils.isEmpty(imgFileName)) {
			iconUrl.append(imgFileName);
		} else {
			iconUrl.append(defaultCuisineIconName);
		}

		return new CuisineRestModel(cuisine.getId(), cuisine.getName(), iconUrl.toString());
	}


	public List<CuisineRestModel> transformAll(Iterable<Cuisine> cuisineEntities) {
		List<CuisineRestModel> retVal = new ArrayList<>();

		cuisineEntities.forEach(cuisine -> retVal.add(transform(cuisine)));

		return retVal;
	}

}
