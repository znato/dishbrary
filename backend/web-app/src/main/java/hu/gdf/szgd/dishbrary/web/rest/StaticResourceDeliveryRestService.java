package hu.gdf.szgd.dishbrary.web.rest;

import hu.gdf.szgd.dishbrary.StaticResourceComponentType;
import hu.gdf.szgd.dishbrary.StaticResourceComponentType.StaticResourceComponentSubType;
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
	public Response getImage(
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
	public Response getImageForRecipe(
			@PathParam("recipeId") Long recipeId,
			@PathParam("imageName") String imageName) {
		try {
			return Response.ok(
					staticResourceService.getResourceForRecipeByName(recipeId, imageName, StaticResourceComponentSubType.IMAGE)
			).build();
		} catch (ResourceNotFoundException e) {
			throw new NotFoundException(e);
		}
	}

	@GET
	@Path("/video/recipe/{recipeId}/{videoName}")
	@Produces({"video/mp4", "video/webm", "video/ogg"})
	public Response getVideoForRecipe(
			@PathParam("recipeId") Long recipeId,
			@PathParam("videoName") String videoName) {
		try {
			return Response.ok(
					staticResourceService.getResourceForRecipeByName(recipeId, videoName, StaticResourceComponentSubType.VIDEO)
			).build();
		} catch (ResourceNotFoundException e) {
			throw new NotFoundException(e);
		}
	}

	@GET
	@Path("/image/user/{userId}/{profileImgName}")
	@Produces("image/png")
	public Response getProfileImgForuser(
			@PathParam("userId") Long userId,
			@PathParam("profileImgName") String profileImgName) {
		try {
			return Response.ok(
					staticResourceService.getProfileImageForUser(userId, profileImgName)
			).build();
		} catch (ResourceNotFoundException e) {
			throw new NotFoundException(e);
		}
	}
}
