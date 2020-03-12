package hu.gdf.szgd.dishbrary.web.rest;

import hu.gdf.szgd.dishbrary.service.IngredientService;
import hu.gdf.szgd.dishbrary.web.model.DishbraryCollectionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static hu.gdf.szgd.dishbrary.web.WebConstants.JSON_WITH_UTF8_ENCODING;
import static hu.gdf.szgd.dishbrary.web.WebConstants.MAX_AGE_30_MIN;

@Service
@Path("/recipe/ingredient")
@Produces(JSON_WITH_UTF8_ENCODING)
@Consumes(JSON_WITH_UTF8_ENCODING)
public class RecipeIngredientRestService {

    @Autowired
    private IngredientService ingredientService;

    @GET
    @Path("/all")
    public Response getAllIngredient() {
        return Response.ok(
                new DishbraryCollectionResponse(ingredientService.getAllIngredient())
        )
                .cacheControl(MAX_AGE_30_MIN)
                .build();
    }
}
