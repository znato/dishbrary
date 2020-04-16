package hu.gdf.szgd.dishbrary.transformer;

import hu.gdf.szgd.dishbrary.StaticResourceComponentType;
import hu.gdf.szgd.dishbrary.db.entity.Cuisine;
import hu.gdf.szgd.dishbrary.web.model.CuisineRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Log4j2
public class CuisineTransformer {

	private static final String BASE_URL = "/rest/resource/image/" + StaticResourceComponentType.CUISINE.name() + "/";

	@Value("${dishbrary.cuisine.images.default.name}")
	private String defaultCuisineIconName;

	public Cuisine transform(CuisineRestModel restModel) {
		Cuisine cuisine = new Cuisine();

		cuisine.setId(restModel.getId());
		cuisine.setName(restModel.getName());

		if (restModel.getIconUrl() != null && !restModel.getIconUrl().contains(defaultCuisineIconName)) {
			cuisine.setIconFileName(restModel.getIconUrl().replace(BASE_URL, ""));
		}

		return cuisine;
	}

	public List<Cuisine> transformAllCuisineRestModel(Iterable<CuisineRestModel> restModels) {
		if (restModels == null) {
			return Collections.emptyList();
		}

		List<Cuisine> retVal = new ArrayList<>();

		restModels.forEach(restModel -> retVal.add(transform(restModel)));

		return retVal;
	}

	public CuisineRestModel transform(Cuisine cuisine) {
		log.debug("Transform Cuisine with name:{} and id: {}", cuisine.getName(), cuisine.getId());

		StringBuilder iconUrl = new StringBuilder(BASE_URL);

		String imgFileName = cuisine.getIconFileName();
		if (!StringUtils.isEmpty(imgFileName)) {
			iconUrl.append(imgFileName);
		} else {
			iconUrl.append(defaultCuisineIconName);
		}

		return new CuisineRestModel(cuisine.getId(), cuisine.getName(), iconUrl.toString());
	}


	public List<CuisineRestModel> transformAll(Iterable<Cuisine> cuisineEntities) {
		if (cuisineEntities == null) {
			return Collections.emptyList();
		}

		List<CuisineRestModel> retVal = new ArrayList<>();

		cuisineEntities.forEach(cuisine -> retVal.add(transform(cuisine)));

		return retVal;
	}

}
