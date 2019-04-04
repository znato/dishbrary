package hu.gdf.szgd.dishbrary.web.rest;

import hu.gdf.szgd.dishbrary.service.CategoryService;
import hu.gdf.szgd.dishbrary.web.model.DishbraryCollectionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static hu.gdf.szgd.dishbrary.web.WebConstants.JSON_WITH_UTF8_ENCODING;

@Service
@Path("/recipe/category")
@Produces(JSON_WITH_UTF8_ENCODING)
@Consumes(JSON_WITH_UTF8_ENCODING)
public class RecipeCategoryRestService {

	@Autowired
	private CategoryService categoryService;

	@GET
	@Path("/all")
	public Response getAllCategories() {
		return Response.ok(
				new DishbraryCollectionResponse<>(categoryService.getAllCategory())
		).build();
	}
}
