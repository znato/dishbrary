package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.db.entity.Recipe;
import hu.gdf.szgd.dishbrary.db.repository.RecipeRepository;
import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import hu.gdf.szgd.dishbrary.service.validation.RecipeValidatorUtil;
import hu.gdf.szgd.dishbrary.transformer.RecipeTransformer;
import hu.gdf.szgd.dishbrary.web.model.RecipeRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;
	@Autowired
	private RecipeTransformer recipeTransformer;

	public RecipeRestModel findRecipeById(Long recipeId) {
		Optional<Recipe> recipe = recipeRepository.findById(recipeId);

		if (!recipe.isPresent()) {
			throw new DishbraryValidationException("Nem létezik recept a következő azonosíto alatt: " + recipeId + "!");
		}

		return recipeTransformer.transform(recipe.get());
	}

	public RecipeRestModel createRecipe(RecipeRestModel recipeRestModel) {
		RecipeValidatorUtil.validateRecipeForCreation(recipeRestModel);

		Recipe recipeToSave = recipeTransformer.transform(recipeRestModel);

		return recipeTransformer.transform(recipeRepository.save(recipeToSave));
	}

	public void saveAdditionalImagesToRecipe(RecipeRestModel recipeRestModel) {
		Optional<Recipe> recipe = recipeRepository.findById(recipeRestModel.getId());

		if (!recipe.isPresent()) {
			throw new DishbraryValidationException("Nem létezik recept a következő azonosíto alatt: " + recipeRestModel.getId() + "!");
		}

		recipe.get().setAdditionalImagesFileNames(recipeRestModel.getAdditionalImagesFileNames());

		recipeRepository.save(recipe.get());
	}
}
