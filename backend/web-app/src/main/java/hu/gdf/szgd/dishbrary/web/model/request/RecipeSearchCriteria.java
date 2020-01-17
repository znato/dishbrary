package hu.gdf.szgd.dishbrary.web.model.request;

import hu.gdf.szgd.dishbrary.web.model.CategoryRestModel;
import hu.gdf.szgd.dishbrary.web.model.CuisineRestModel;
import hu.gdf.szgd.dishbrary.web.model.IngredientRestModel;
import lombok.Data;

import java.util.List;

@Data
public class RecipeSearchCriteria {

	private String plainTextSearch;
	private List<IngredientRestModel> ingredientList;
	private List<CuisineRestModel> cuisineList;
	private List<CategoryRestModel> categoryList;

}
