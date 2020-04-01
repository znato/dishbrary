package hu.gdf.szgd.dishbrary.web.rest;

import hu.gdf.szgd.dishbrary.security.annotation.RecipeId;
import hu.gdf.szgd.dishbrary.security.annotation.ValidateRecipeBelongsToLoggedInUser;
import hu.gdf.szgd.dishbrary.service.StaticResourceService;
import hu.gdf.szgd.dishbrary.web.model.DishbraryResponse;
import hu.gdf.szgd.dishbrary.web.model.FileResource;
import lombok.extern.log4j.Log4j2;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static hu.gdf.szgd.dishbrary.web.WebConstants.JSON_WITH_UTF8_ENCODING;

@Service
@Path("/resource/")
@Consumes({MediaType.MULTIPART_FORM_DATA})
@Produces(JSON_WITH_UTF8_ENCODING)
@Log4j2
public class ResourceRestService {

	@Autowired
	private StaticResourceService staticResourceService;

	@POST
	@Path("recipe/{recipeId}/image/upload")
	@PreAuthorize("hasAuthority('WRITE_RECIPE')")
	@ValidateRecipeBelongsToLoggedInUser
	public Response uploadImage(@PathParam("recipeId") @RecipeId Long recipeId,
								@Multipart("selectedCoverImageFileName") String selectedCoverImageFileName,
								final List<Attachment> attachments) {

		List<FileResource> fileResources = new ArrayList<>(attachments.size());

		for (Attachment attachment : attachments) {
			String fileName = attachment.getContentDisposition().getFilename();

			//if filename is not filled ignore the bodypart (it can be null for non image entries like selectedCoverImageFileName which appended manually)
			if (fileName == null) {
				continue;
			}

			try {
				fileResources.add(new FileResource(fileName, attachment.getDataHandler().getInputStream()));
			} catch (IOException e) {
				log.warn("File data inputstream cannot be obtained!", e);
			}
		}

		staticResourceService.uploadRecipeImages(recipeId, selectedCoverImageFileName, fileResources);

		return Response.ok(new DishbraryResponse<>("A kép(ek) sikeresen mentve!")).build();
	}

	@DELETE
	@Path("recipe/{recipeId}/image/deleteAll")
	@PreAuthorize("hasAuthority('WRITE_RECIPE')")
	@ValidateRecipeBelongsToLoggedInUser
	public Response deleteAllImages(@PathParam("recipeId") @RecipeId Long recipeId) {
		staticResourceService.deleteAllRecipeImages(recipeId);

		return Response.ok(new DishbraryResponse<>("A kép(ek) sikeresen törölve!")).build();
	}

	@POST
	@Path("recipe/{recipeId}/video/upload")
	@PreAuthorize("hasAuthority('WRITE_RECIPE')")
	@ValidateRecipeBelongsToLoggedInUser
	public Response uploadVideo(@PathParam("recipeId") @RecipeId Long recipeId,
								@Multipart(value = "videoInput", required = false) Attachment videoInput) {

		if (videoInput == null || StringUtils.isEmpty(videoInput.getContentDisposition().getFilename())) {
			throw new ClientErrorException(Response.Status.BAD_REQUEST);
		}

		FileResource fileResource = null;

		try {
			fileResource = new FileResource(videoInput.getContentDisposition().getFilename(), videoInput.getDataHandler().getInputStream());
		} catch (IOException e) {
			log.warn("File data inputstream cannot be obtained!", e);
		}

		staticResourceService.uploadRecipeVideo(recipeId, fileResource);

		return Response.ok(new DishbraryResponse<>("A videó sikeresen mentve!")).build();
	}

	@DELETE
	@Path("recipe/{recipeId}/video")
	@PreAuthorize("hasAuthority('WRITE_RECIPE')")
	@ValidateRecipeBelongsToLoggedInUser
	public Response deleteVideo(@PathParam("recipeId") @RecipeId Long recipeId) {

		staticResourceService.deleteRecipeVideo(recipeId);

		return Response.ok(
				new DishbraryResponse<>("A videó sikeresen törölve!")
		).build();
	}
}
