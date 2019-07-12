package hu.gdf.szgd.dishbrary.transformer;

import static hu.gdf.szgd.dishbrary.transformer.TransformerUtil.minuteToMillis;
import static hu.gdf.szgd.dishbrary.transformer.TransformerUtil.millisToMinute;

import hu.gdf.szgd.dishbrary.db.entity.Recipe;
import hu.gdf.szgd.dishbrary.db.entity.RecipeIngredient;
import hu.gdf.szgd.dishbrary.security.SecurityUtils;
import hu.gdf.szgd.dishbrary.web.model.RecipeIngredientRestModel;
import hu.gdf.szgd.dishbrary.web.model.RecipeRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class RecipeTransformer {

	@Autowired
	private GenericReflectionBasedTransformer genericTransformer;
	@Autowired
	private UserTransformer userTransformer;
	@Autowired
	private IngredientTransformer ingredientTransformer;
	@Autowired
	private CategoryTransformer categoryTransformer;
	@Autowired
	private CuisineTransformer cuisineTransformer;

	public Recipe transform(RecipeRestModel recipeRestModel) {
		Recipe recipe = genericTransformer.transform(recipeRestModel, new Recipe());

		recipe.setOwner(userTransformer.transformDishbraryUser(SecurityUtils.getDishbraryUserFromContext()));

		recipe.setPreparationTimeInMillis(minuteToMillis(recipeRestModel.getPreparationTimeInMinute()));
		recipe.setCookTimeInMillis(minuteToMillis(recipeRestModel.getCookTimeInMinute()));

		recipe.setCategories(categoryTransformer.transformAllCategoryRestModel(recipeRestModel.getCategories()));
		recipe.setCuisines(cuisineTransformer.transformAllCuisineRestModel(recipeRestModel.getCuisines()));

		List<RecipeIngredient> recipeIngredients = new ArrayList<>(recipeRestModel.getIngredients().size());
		for (RecipeIngredientRestModel ingredientRestModel : recipeRestModel.getIngredients()) {
			RecipeIngredient recipeIngredient = new RecipeIngredient();

			recipeIngredient.setRecipe(recipe);
			recipeIngredient.setIngredient(ingredientTransformer.transform(ingredientRestModel.getIngredient()));
			recipeIngredient.setQuantity(ingredientRestModel.getQuantity());
			recipeIngredient.setSelectedUnit(ingredientRestModel.getSelectedUnit());

			recipeIngredients.add(recipeIngredient);
		}

		recipe.setIngredients(recipeIngredients);

		return recipe;
	}

	public RecipeRestModel transform(Recipe recipe) {
		RecipeRestModel restModel = genericTransformer.transform(recipe, new RecipeRestModel());

		restModel.setPreparationTimeInMinute(millisToMinute(recipe.getPreparationTimeInMillis()));
		restModel.setCookTimeInMinute(millisToMinute(recipe.getCookTimeInMillis()));

		restModel.setCategories(categoryTransformer.transformAll(recipe.getCategories()));
		restModel.setCuisines(cuisineTransformer.transformAll(recipe.getCuisines()));

		List<RecipeIngredientRestModel> ingredientRestModels = new ArrayList<>(recipe.getIngredients().size());

		for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
			ingredientRestModels.add(new RecipeIngredientRestModel(
					recipeIngredient.getId(),
					null,
					ingredientTransformer.transform(recipeIngredient.getIngredient()),
					recipeIngredient.getQuantity(),
					recipeIngredient.getSelectedUnit()
			));
		}

		restModel.setIngredients(ingredientRestModels);

		return restModel;
	}

	public List<RecipeRestModel> transformAll(Iterable<Recipe> recipeEntities) {
		List<RecipeRestModel> retVal = new ArrayList<>();

		recipeEntities.forEach(recipeEntity -> retVal.add(transform(recipeEntity)));

		return retVal;
	}
}
