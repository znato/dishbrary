package hu.gdf.szgd.dishbrary.test.web.validation;

import hu.gdf.szgd.dishbrary.db.entity.RecipeIngredient;
import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import hu.gdf.szgd.dishbrary.test.AbstractTest;
import hu.gdf.szgd.dishbrary.web.model.IngredientRestModel;
import hu.gdf.szgd.dishbrary.web.model.RecipeIngredientRestModel;
import hu.gdf.szgd.dishbrary.web.model.RecipeRestModel;
import hu.gdf.szgd.dishbrary.web.validation.RecipeValidationUtil;
import org.junit.Test;

import java.util.Arrays;

public class RecipeValidationTest extends AbstractTest {

	@Test(expected = DishbraryValidationException.class)
	public void testEmptyUserName() {
		RecipeRestModel recipe = new RecipeRestModel();
		recipe.setName("     ");
		recipe.setPortion(2);
		recipe.setInstruction("df");
		recipe.setIngredients(Arrays.asList(new RecipeIngredientRestModel()));

		RecipeValidationUtil.validateRecipe(recipe);
	}

	@Test(expected = DishbraryValidationException.class)
	public void testEmptyInstruction() {
		RecipeRestModel recipe = new RecipeRestModel();
		recipe.setName("name");
		recipe.setPortion(2);
		recipe.setInstruction("     ");
		recipe.setIngredients(Arrays.asList(new RecipeIngredientRestModel()));

		RecipeValidationUtil.validateRecipe(recipe);
	}

	@Test(expected = DishbraryValidationException.class)
	public void testEmptyPortion() {
		RecipeRestModel recipe = new RecipeRestModel();
		recipe.setName("name");
		recipe.setInstruction("df");
		recipe.setIngredients(Arrays.asList(new RecipeIngredientRestModel()));

		RecipeValidationUtil.validateRecipe(recipe);
	}

	@Test(expected = DishbraryValidationException.class)
	public void testEmptyIngredients() {
		RecipeRestModel recipe = new RecipeRestModel();
		recipe.setName("name");
		recipe.setPortion(2);
		recipe.setInstruction("df");

		RecipeValidationUtil.validateRecipe(recipe);
	}

	@Test(expected = DishbraryValidationException.class)
	public void testXSSAttackInRecipeName() {
		RecipeRestModel recipe = new RecipeRestModel();
		recipe.setName("hello <script>alert('hello')</script>");
		recipe.setPortion(2);
		recipe.setInstruction("df");
		recipe.setIngredients(Arrays.asList(new RecipeIngredientRestModel()));

		RecipeValidationUtil.validateRecipe(recipe);
	}

	@Test(expected = DishbraryValidationException.class)
	public void testXSSAttackInRecipeInstruction() {
		RecipeRestModel recipe = new RecipeRestModel();
		recipe.setInstruction("hello <script>alert('hello')</script>");
		recipe.setPortion(2);
		recipe.setName("df");
		recipe.setIngredients(Arrays.asList(new RecipeIngredientRestModel()));

		RecipeValidationUtil.validateRecipe(recipe);
	}

	@Test
	public void testValidRecipe() {
		RecipeRestModel recipe = new RecipeRestModel();
		recipe.setName("Test Recipe");
		recipe.setInstruction("test instruction");
		IngredientRestModel ingredient = new IngredientRestModel();
		ingredient.setId(1l);
		recipe.setIngredients(Arrays.asList(new RecipeIngredientRestModel(null, null, ingredient, 1, RecipeIngredient.SelectableUnit.kg)));
		recipe.setPortion(2);

		recipe.setIngredients(Arrays.asList(new RecipeIngredientRestModel()));
	}
}
