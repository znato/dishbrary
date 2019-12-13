package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.db.entity.Ingredient;
import hu.gdf.szgd.dishbrary.db.entity.Recipe;
import hu.gdf.szgd.dishbrary.db.entity.RecipeIngredient;
import hu.gdf.szgd.dishbrary.db.repository.IngredientRepository;
import hu.gdf.szgd.dishbrary.db.repository.RecipeRepository;
import hu.gdf.szgd.dishbrary.security.SecurityUtils;
import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import hu.gdf.szgd.dishbrary.service.validation.RecipeValidatorUtil;
import hu.gdf.szgd.dishbrary.transformer.RecipeTransformer;
import hu.gdf.szgd.dishbrary.web.model.PageableRestModel;
import hu.gdf.szgd.dishbrary.web.model.RecipeIngredientRestModel;
import hu.gdf.szgd.dishbrary.web.model.RecipeRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class RecipeService {

	private static final BigDecimal HUNDRED = new BigDecimal(100);

	@Autowired
	private RecipeRepository recipeRepository;
	@Autowired
	private IngredientRepository ingredientRepository;
	@Autowired
	private RecipeTransformer recipeTransformer;

	@Transactional
	public RecipeRestModel findRecipeById(Long recipeId) {
		Optional<Recipe> recipe = recipeRepository.findById(recipeId);

		if (!recipe.isPresent()) {
			throw new DishbraryValidationException("Nem létezik recept a következő azonosíto alatt: " + recipeId + "!");
		}

		return recipeTransformer.transform(recipe.get());
	}

	@Transactional
	public PageableRestModel<RecipeRestModel> findPageableRecipesByUserId(Long userId, int pageNumber) {
		Page<Recipe> userRecipesPage = recipeRepository.findByOwnerId(
				userId,
				PageRequest.of(pageNumber, RecipeRepository.DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "creationDate")));

		List<RecipeRestModel> restModels = recipeTransformer.transformAll(userRecipesPage);

		return new PageableRestModel<>(restModels, userRecipesPage.getTotalElements(), userRecipesPage.getTotalPages());
	}

	@Transactional
	public RecipeRestModel saveRecipe(RecipeRestModel recipeRestModel) {
		if (recipeRestModel.getId() != null) {
			return updateRecipe(recipeRestModel);
		}

		RecipeValidatorUtil.validateRecipeForCreation(recipeRestModel);

		Recipe recipeToSave = recipeTransformer.transform(recipeRestModel);
		recipeToSave.setAdditionalInfo(createAdditionalInfo(recipeRestModel));

		return recipeTransformer.transform(recipeRepository.save(recipeToSave));
	}

	@Transactional
	public RecipeRestModel updateRecipe(RecipeRestModel recipeRestModel) {
		Objects.requireNonNull(recipeRestModel.getId(), "A receptet nem sikerült azonosítani mert hiányzik az id mező!");

		RecipeValidatorUtil.validateRecipeForCreation(recipeRestModel);

		Optional<Recipe> recipeToUpdateHolder = recipeRepository.findById(recipeRestModel.getId());
		if (!recipeToUpdateHolder.isPresent()) {
			throw new DishbraryValidationException("Nem létezik recept a következő azonosíto alatt: " + recipeRestModel.getId() + "!");
		}

		Recipe recipeToUpdate = recipeToUpdateHolder.get();

		recipeToUpdate = recipeTransformer.transformForUpdate(recipeToUpdate, recipeRestModel);
		recipeToUpdate.setAdditionalInfo(createAdditionalInfo(recipeRestModel));

		return recipeTransformer.transform(recipeRepository.save(recipeToUpdate));
	}

	private Recipe.AdditionalInfo createAdditionalInfo(RecipeRestModel recipeRestModel) {
		BigDecimal energyKcalSum = new BigDecimal(0);
		BigDecimal proteinSum = new BigDecimal(0);
		BigDecimal fatSum = new BigDecimal(0);
		BigDecimal carbohydrateSum = new BigDecimal(0);

		for (RecipeIngredientRestModel ingredientData : recipeRestModel.getIngredients()) {
			Optional<Ingredient> foundIngredient = ingredientRepository.findById(ingredientData.getIngredient().getId());
			if (!foundIngredient.isPresent()) {
				throw new DishbraryValidationException("Nem létezik hozzavaló a következő azonosíto alatt: " + ingredientData.getIngredient().getId() + "!");
			}

			Ingredient ingredient = foundIngredient.get();

			BigDecimal actualQuantityMultiplier = new BigDecimal(ingredientData.getQuantity())
					.multiply(ingredientData.getSelectedUnit().getMultiplierBigDecimalValue());

			//calorie related data stored for 100 g/ml if unit is not measured in pieces
			if (!RecipeIngredient.SelectableUnit.db.equals(ingredientData.getSelectedUnit())) {
				actualQuantityMultiplier = actualQuantityMultiplier.divide(HUNDRED);
			}

			energyKcalSum = energyKcalSum.add(new BigDecimal(ingredient.getEnergyKcal()).multiply(actualQuantityMultiplier));
			proteinSum = proteinSum.add(ingredient.getProtein().multiply(actualQuantityMultiplier));
			fatSum = fatSum.add(ingredient.getFat().multiply(actualQuantityMultiplier));
			carbohydrateSum = carbohydrateSum.add(ingredient.getCarbohydrate().multiply(actualQuantityMultiplier));
		}

		BigDecimal portion = new BigDecimal(recipeRestModel.getPortion());
		energyKcalSum = energyKcalSum.divide(portion, RoundingMode.HALF_UP);
		proteinSum = proteinSum.divide(portion, RoundingMode.HALF_UP);
		fatSum = fatSum.divide(portion, RoundingMode.HALF_UP);
		carbohydrateSum.divide(portion, RoundingMode.HALF_UP);

		return new Recipe.AdditionalInfo(energyKcalSum.toString(), proteinSum.toString(), fatSum.toString(), carbohydrateSum.toString());
	}

	@Transactional
	public void saveVideoToRecipe(RecipeRestModel recipeRestModel) {
		Optional<Recipe> recipe = recipeRepository.findById(recipeRestModel.getId());

		if (!recipe.isPresent()) {
			throw new DishbraryValidationException("Nem létezik recept a következő azonosíto alatt: " + recipeRestModel.getId() + "!");
		}

		Recipe recipeEntity = recipe.get();

		recipeEntity.setVideoFileName(recipeRestModel.getVideoFileName());

		recipeRepository.save(recipeEntity);
	}

	@Transactional
	public void saveImagesToRecipe(RecipeRestModel recipeRestModel) {
		Optional<Recipe> recipe = recipeRepository.findById(recipeRestModel.getId());

		if (!recipe.isPresent()) {
			throw new DishbraryValidationException("Nem létezik recept a következő azonosíto alatt: " + recipeRestModel.getId() + "!");
		}

		Recipe recipeEntity = recipe.get();

		recipeEntity.setCoverImageFileName(recipeRestModel.getCoverImageFileName());

		recipeEntity.setAdditionalImagesFileNames(recipeRestModel.getAdditionalImagesFileNames());

		recipeRepository.save(recipeEntity);
	}
}
