package hu.gdf.szgd.dishbrary.transformer;

import hu.gdf.szgd.dishbrary.db.criteria.RecipeSearchCriteria;
import hu.gdf.szgd.dishbrary.web.model.CategoryRestModel;
import hu.gdf.szgd.dishbrary.web.model.CuisineRestModel;
import hu.gdf.szgd.dishbrary.web.model.IngredientRestModel;
import hu.gdf.szgd.dishbrary.web.model.request.RecipeSearchCriteriaRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j2
public class RecipeSearchCriteriaTransformer {

	private static final List<Long> EMPTY_ID_LIST = Arrays.asList(0L);

	public RecipeSearchCriteria transform(RecipeSearchCriteriaRestModel criteriaRestModel) {
		RecipeSearchCriteria criteria = new RecipeSearchCriteria();

		if (StringUtils.hasText(criteriaRestModel.getPlainTextSearch())) {
			criteria.setPlainTextSearch(criteriaRestModel.getPlainTextSearch());
		} else {
			criteria.setPlainTextEmpty(true);
		}

		if (!CollectionUtils.isEmpty(criteriaRestModel.getIngredientList())) {
			criteria.setIngredientIdList(
					criteriaRestModel.getIngredientList().stream().map(IngredientRestModel::getId).collect(Collectors.toList())
			);
		} else {
			criteria.setIngredientsEmpty(true);
			criteria.setIngredientIdList(EMPTY_ID_LIST);
		}

		if (!CollectionUtils.isEmpty(criteriaRestModel.getCategoryList())) {
			criteria.setCategoryIdList(criteriaRestModel.getCategoryList().stream().map(CategoryRestModel::getId).collect(Collectors.toList()));
		} else {
			criteria.setCategoriesEmpty(true);
			criteria.setCategoryIdList(EMPTY_ID_LIST);
		}

		if (!CollectionUtils.isEmpty(criteriaRestModel.getCuisineList())) {
			criteria.setCuisineIdList(criteriaRestModel.getCuisineList().stream().map(CuisineRestModel::getId).collect(Collectors.toList()));
		} else {
			criteria.setCuisinesEmpty(true);
			criteria.setCuisineIdList(EMPTY_ID_LIST);
		}

		return criteria;
	}

}
