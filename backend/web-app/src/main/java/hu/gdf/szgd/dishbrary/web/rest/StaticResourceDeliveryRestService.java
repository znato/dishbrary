package hu.gdf.szgd.dishbrary.web.rest;

import hu.gdf.szgd.dishbrary.StaticResourceComponentType;
import hu.gdf.szgd.dishbrary.service.StaticResourceService;
import hu.gdf.szgd.dishbrary.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Service
@Path("/resource/")
public class StaticResourceDeliveryRestService {

	@Autowired
	private StaticResourceService staticResourceService;

	@GET
	@Path("/image/{component}/{imageName}")
	@Produces("image/png")
	public Response getFullImage(
			@PathParam("component") String component,
			@PathParam("imageName") String imageName) {
		StaticResourceComponentType componentType = StaticResourceComponentType.valueOf(component.toUpperCase());

		try {
			return Response.ok(
					staticResourceService.getImageForComponentByName(componentType, imageName)
			).build();
		} catch (ResourceNotFoundException e) {
			throw new NotFoundException(e);
		}
	}

	@GET
	@Path("/image/recipe/{recipeId}/{imageName}")
	@Produces("image/png")
	public Response getFullImageForRecipe(
			@PathParam("recipeId") Long recipeId,
			@PathParam("imageName") String imageName) {
		try {
			return Response.ok(
					staticResourceService.getImageForRecipeByName(recipeId, imageName)
			).build();
		} catch (ResourceNotFoundException e) {
			throw new NotFoundException(e);
		}
	}
}
