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

	private static final TransformerConfig USER_TRANSFORMER_CONFIG = TransformerConfig.excludeFields("grantedAuthorities");

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
		return transform(recipe, null);
	}

	public RecipeRestModel transform(Recipe recipe, TransformerConfig config) {
		RecipeRestModel restModel = genericTransformer.transform(recipe, new RecipeRestModel(), config);

		if (!TransformerConfig.isFieldExcludedInConfig(config, "preparationTimeInMinute")) {
			restModel.setPreparationTimeInMinute(millisToMinute(recipe.getPreparationTimeInMillis()));
		}

		if (!TransformerConfig.isFieldExcludedInConfig(config, "cookTimeInMinute")) {
			restModel.setCookTimeInMinute(millisToMinute(recipe.getCookTimeInMillis()));
		}

		if (!TransformerConfig.isFieldExcludedInConfig(config, "categories")) {
			restModel.setCategories(categoryTransformer.transformAll(recipe.getCategories()));
		}

		if (!TransformerConfig.isFieldExcludedInConfig(config, "cuisines")) {
			restModel.setCuisines(cuisineTransformer.transformAll(recipe.getCuisines()));
		}

		if (StringUtils.isEmpty(recipe.getCoverImageFileName())) {
			restModel.setCoverImageFileName(defaultCoverImageFileName);
		}

		if (!TransformerConfig.isFieldExcludedInConfig(config, "additionalImagesFileNames") && recipe.getAdditionalImagesFileNames() != null) {
			restModel.setAdditionalImagesFileNames(new ArrayList<>(recipe.getAdditionalImagesFileNames()));
		}

		if (!TransformerConfig.isFieldExcludedInConfig(config, "ingredients")) {
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
		}

		if (!TransformerConfig.isFieldExcludedInConfig(config, "calorieInfo")) {
			Recipe.AdditionalInfo additionalInfo = recipe.getAdditionalInfo();

			if (additionalInfo != null) {
				restModel.setCalorieInfo(new RecipeRestModel.CalorieInfo(
						additionalInfo.getEnergyKcal(),
						additionalInfo.getProtein(),
						additionalInfo.getFat(),
						additionalInfo.getCarbohydrate()
				));
			}
		}

		if (!TransformerConfig.isFieldExcludedInConfig(config, "owner")) {
			restModel.setOwner(userTransformer.transformUser(recipe.getOwner(), USER_TRANSFORMER_CONFIG));
		}

		//users can edit their own recipe if they are logged in
		boolean userAuthenticated = SecurityUtils.isSessionAuthenticated();
		boolean editable = userAuthenticated && SecurityUtils.getDishbraryUserFromContext().getId().equals(recipe.getOwner().getId());
		restModel.setEditable(editable);
		//like functionality is available only for logged on users, users are not able to like their own recipes
		restModel.setLikeable(userAuthenticated && !editable);

		return restModel;
	}

	public List<RecipeRestModel> transformAll(Iterable<Recipe> recipeEntities) {
		return transformAll(recipeEntities, null);
	}

	public List<RecipeRestModel> transformAll(Iterable<Recipe> recipeEntities, TransformerConfig config) {
		List<RecipeRestModel> retVal = new ArrayList<>();

		recipeEntities.forEach(recipeEntity -> retVal.add(transform(recipeEntity, config)));

		return retVal;
	}
}
