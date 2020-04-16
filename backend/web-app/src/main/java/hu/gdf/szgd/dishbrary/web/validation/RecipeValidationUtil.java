package hu.gdf.szgd.dishbrary.web.validation;

import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import hu.gdf.szgd.dishbrary.web.model.RecipeRestModel;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class RecipeValidationUtil {

	private static final String RECIPE_VALIDATION_BASE_MESAGE = "A recept ellenőrzése közben a következő hibak léptek fel:";

	public static void validateRecipe(RecipeRestModel recipeRestModel) {
		StringBuilder errorMessageBuilder = null;

		if (!StringUtils.hasText(recipeRestModel.getName())) {
			errorMessageBuilder = createOrRetrieveErrorMessageBuilder(errorMessageBuilder, RECIPE_VALIDATION_BASE_MESAGE)
					.append("\nA recept neve nincs kitöltve!");
		}

		if (recipeRestModel.getPortion() == null) {
			errorMessageBuilder = createOrRetrieveErrorMessageBuilder(errorMessageBuilder, RECIPE_VALIDATION_BASE_MESAGE)
					.append("\nAz adagok száma nincs kitöltve!");
		} else if (recipeRestModel.getPortion() < 1) {
			errorMessageBuilder = createOrRetrieveErrorMessageBuilder(errorMessageBuilder, RECIPE_VALIDATION_BASE_MESAGE)
					.append("\nAz adagok száma nem lehet egynél kisebb!");
		}

		if (!StringUtils.hasText(recipeRestModel.getInstruction())) {
			errorMessageBuilder = createOrRetrieveErrorMessageBuilder(errorMessageBuilder, RECIPE_VALIDATION_BASE_MESAGE)
					.append("\nA recept leírása hiányzik!");
		}

		if (CollectionUtils.isEmpty(recipeRestModel.getIngredients())) {
			errorMessageBuilder = createOrRetrieveErrorMessageBuilder(errorMessageBuilder, RECIPE_VALIDATION_BASE_MESAGE)
					.append("\nA recept hozzávalói hiányoznak!");
		}

		if (errorMessageBuilder != null) {
			throw new DishbraryValidationException(errorMessageBuilder.toString());
		}

		CommonValidationUtil.validateAgainstXSSAttack(recipeRestModel.getName(),
				new DishbraryValidationException("A recept neve nem megengedett karaktereket tartalmaz!"));

		CommonValidationUtil.validateAgainstXSSAttack(recipeRestModel.getInstruction(),
				new DishbraryValidationException("A recept leírása nem megengedett karaktereket tartalmaz!"));
	}

	private static StringBuilder createOrRetrieveErrorMessageBuilder(StringBuilder errorMessageBuilder, String initialText) {
		if (errorMessageBuilder == null) {
			return new StringBuilder(initialText);
		}

		return errorMessageBuilder;
	}
}
