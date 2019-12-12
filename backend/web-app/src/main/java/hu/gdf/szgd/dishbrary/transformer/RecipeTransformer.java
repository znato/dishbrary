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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class RecipeTransformer {

	@Value("${dishbrary.recipe.images.defaultCoverImage.name}")
	private String defaultCoverImageFileName;

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

	public Recipe transformForUpdate(Recipe recipeToUpdate, RecipeRestModel recipeRestModel) {
		recipeToUpdate.setName(recipeRestModel.getName());
		recipeToUpdate.setInstruction(recipeRestModel.getInstruction());
		recipeToUpdate.setPortion(recipeRestModel.getPortion());
		recipeToUpdate.setPreparationTimeInMillis(minuteToMillis(recipeRestModel.getPreparationTimeInMinute()));
		recipeToUpdate.setCookTimeInMillis(minuteToMillis(recipeRestModel.getCookTimeInMinute()));

		recipeToUpdate.setCategories(categoryTransformer.transformAllCategoryRestModel(recipeRestModel.getCategories()));
		recipeToUpdate.setCuisines(cuisineTransformer.transformAllCuisineRestModel(recipeRestModel.getCuisines()));

		recipeToUpdate.getIngredients().clear();

		List<RecipeIngredient> recipeIngredients = new ArrayList<>(recipeRestModel.getIngredients().size());
		for (RecipeIngredientRestModel ingredientRestModel : recipeRestModel.getIngredients()) {
			RecipeIngredient recipeIngredient = new RecipeIngredient();

			recipeIngredient.setRecipe(recipeToUpdate);
			recipeIngredient.setIngredient(ingredientTransformer.transform(ingredientRestModel.getIngredient()));
			recipeIngredient.setQuantity(ingredientRestModel.getQuantity());
			recipeIngredient.setSelectedUnit(ingredientRestModel.getSelectedUnit());

			recipeIngredients.add(recipeIngredient);
		}

		recipeToUpdate.getIngredients().addAll(recipeIngredients);

		return recipeToUpdate;
	}

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

		if (StringUtils.isEmpty(recipe.getCoverImageFileName())) {
			restModel.setCoverImageFileName(defaultCoverImageFileName);
		}

		if (recipe.getAdditionalImagesFileNames() != null) {
			restModel.setAdditionalImagesFileNames(new ArrayList<>(recipe.getAdditionalImagesFileNames()));
		}

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

		Recipe.AdditionalInfo additionalInfo = recipe.getAdditionalInfo();

		if (additionalInfo != null) {
			restModel.setCalorieInfo(new RecipeRestModel.CalorieInfo(
					additionalInfo.getEnergyKcal(),
					additionalInfo.getProtein(),
					additionalInfo.getFat(),
					additionalInfo.getCarbohydrate()
			));
		}

		restModel.setOwner(userTransformer.transformUser(recipe.getOwner()));

		//users can edit their own recipe if they are logged in
		boolean editable = SecurityUtils.isSessionAuthenticated() && SecurityUtils.getDishbraryUserFromContext().getId().equals(recipe.getOwner().getId());
		restModel.setEditable(editable);

		return restModel;
	}

	public List<RecipeRestModel> transformAll(Iterable<Recipe> recipeEntities) {
		List<RecipeRestModel> retVal = new ArrayList<>();

		recipeEntities.forEach(recipeEntity -> retVal.add(transform(recipeEntity)));

		return retVal;
	}
}
