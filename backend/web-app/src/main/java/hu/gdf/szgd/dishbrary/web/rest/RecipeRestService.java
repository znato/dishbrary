package hu.gdf.szgd.dishbrary.web.rest;

import hu.gdf.szgd.dishbrary.security.DishbraryUser;
import hu.gdf.szgd.dishbrary.security.SecurityUtils;
import hu.gdf.szgd.dishbrary.security.annotation.RecipeId;
import hu.gdf.szgd.dishbrary.security.annotation.ValidateRecipeBelongsToLoggedInUser;
import hu.gdf.szgd.dishbrary.service.RecipeService;
import hu.gdf.szgd.dishbrary.web.model.DishbraryResponse;
import hu.gdf.szgd.dishbrary.web.model.RecipeRestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static hu.gdf.szgd.dishbrary.web.WebConstants.JSON_WITH_UTF8_ENCODING;

@Service
@Path("/recipe")
@Produces(JSON_WITH_UTF8_ENCODING)
@Consumes(JSON_WITH_UTF8_ENCODING)
public class RecipeRestService {

	@Autowired
	private RecipeService recipeService;

	@GET
	@Path("/{recipeId}")
	public Response getRecipeById(@PathParam("recipeId") Long recipeId) {
		return Response.ok(
				new DishbraryResponse<>(recipeService.findRecipeById(recipeId))
		).build();
	}

	@GET
	@Path("/recipes/random")
	public Response getRandomRecipes() {
		return Response.ok(
				new DishbraryResponse<>(recipeService.findRandomRecipes())
		).build();
	}

	@GET
	@Path("/my-recipes")
	@PreAuthorize("hasRole('SIMPLE_USER')")
	public Response getMyRecipes(@QueryParam("page") int pageNumber) {
		DishbraryUser loggedInUser = SecurityUtils.getDishbraryUserFromContext();

		return Response.ok(
				new DishbraryResponse<>(
						recipeService.findPageableRecipesByUserId(loggedInUser.getId(), pageNumber)
				)
		).build();
	}

	@PUT
	@Path("/create")
	@PreAuthorize("hasAuthority('WRITE_RECIPE')")
	public Response createRecipe(RecipeRestModel recipeToSave) {
		return Response.ok(
				new DishbraryResponse<>(recipeService.saveRecipe(recipeToSave))
		).build();
	}

	@POST
	@Path("/update/{recipeId}")
	@PreAuthorize("hasAuthority('WRITE_RECIPE')")
	@ValidateRecipeBelongsToLoggedInUser
	public Response createRecipe(@PathParam("recipeId") @RecipeId Long recipeId, RecipeRestModel recipeToUpdate) {
		return Response.ok(
				new DishbraryResponse<>(recipeService.updateRecipe(recipeToUpdate))
		).build();
	}

	@DELETE
	@Path("/delete/{recipeId}")
	@PreAuthorize("hasAuthority('WRITE_RECIPE')")
	@ValidateRecipeBelongsToLoggedInUser
	public Response deleteRecipe(@PathParam("recipeId") @RecipeId Long recipeId) {
		recipeService.deleteRecipeById(recipeId);

		return Response.ok(
				new DishbraryResponse<>("A recept sikeresen törölve!")
		).build();
	}
}
