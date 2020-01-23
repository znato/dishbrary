package hu.gdf.szgd.dishbrary.db.criteria;

import lombok.Data;

import java.util.List;

@Data
public class RecipeSearchCriteria {

	private String plainTextSearch;
	private List<Long> ingredientIdList;
	private List<Long> cuisineIdList;
	private List<Long> categoryIdList;

	private boolean plainTextEmpty;
	private boolean ingredientsEmpty;
	private boolean cuisinesEmpty;
	private boolean categoriesEmpty;
}
