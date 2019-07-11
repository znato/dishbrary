package hu.gdf.szgd.dishbrary.web.rest;

import hu.gdf.szgd.dishbrary.service.RecipeService;
import hu.gdf.szgd.dishbrary.web.model.DishbraryResponse;
import hu.gdf.szgd.dishbrary.web.model.RecipeRestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static hu.gdf.szgd.dishbrary.web.WebConstants.JSON_WITH_UTF8_ENCODING;

@Service
@Path("/recipe")
@Produces(JSON_WITH_UTF8_ENCODING)
@Consumes(JSON_WITH_UTF8_ENCODING)
public class RecipeRestService {

	@Autowired
	private RecipeService recipeService;

	@PUT
	@Path("/create")
	@PreAuthorize("hasAuthority('WRITE_RECIPE')")
	public Response createRecipe(RecipeRestModel recipeToSave) {
		return Response.ok(
				new DishbraryResponse<>(recipeService.createRecipe(recipeToSave))
		).build();
	}
}
