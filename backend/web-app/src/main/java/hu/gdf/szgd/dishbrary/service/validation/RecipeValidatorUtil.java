package hu.gdf.szgd.dishbrary.service.validation;

import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import hu.gdf.szgd.dishbrary.web.model.RecipeRestModel;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class RecipeValidatorUtil {

	private static final String RECIPE_VALIDATION_BASE_MESAGE = "A recept ellenőrzése közben a következő hibak léptek fel:";

	public static void validateRecipeForCreation(RecipeRestModel recipeRestModel) {
		StringBuilder errorMessageBuilder = null;

		if (StringUtils.isEmpty(recipeRestModel.getName())) {
			createOrRetrieveErrorMessageBuilder(errorMessageBuilder, RECIPE_VALIDATION_BASE_MESAGE)
					.append("\nA recept neve nincs kitöltve!");
		}

		if (recipeRestModel.getPortion() == null) {
			createOrRetrieveErrorMessageBuilder(errorMessageBuilder, RECIPE_VALIDATION_BASE_MESAGE)
					.append("\nAz adagok száma nincs kitöltve!");
		} else if (recipeRestModel.getPortion() < 1) {
			createOrRetrieveErrorMessageBuilder(errorMessageBuilder, RECIPE_VALIDATION_BASE_MESAGE)
					.append("\nAz adagok száma nem lehet egynél kisebb!");
		}

		if (StringUtils.isEmpty(recipeRestModel.getInstruction())) {
			createOrRetrieveErrorMessageBuilder(errorMessageBuilder, RECIPE_VALIDATION_BASE_MESAGE)
					.append("\nA recept leírása hiányzik!");
		}

		if (CollectionUtils.isEmpty(recipeRestModel.getIngredients())) {
			createOrRetrieveErrorMessageBuilder(errorMessageBuilder, RECIPE_VALIDATION_BASE_MESAGE)
					.append("\nA recept hozzávalói hiányoznak!");
		}

		if (errorMessageBuilder != null) {
			throw new DishbraryValidationException(errorMessageBuilder.toString());
		}
	}

	private static StringBuilder createOrRetrieveErrorMessageBuilder(StringBuilder errorMessageBuilder, String initialText) {
		if (errorMessageBuilder == null) {
			return new StringBuilder(initialText);
		}

		return errorMessageBuilder;
	}
}
