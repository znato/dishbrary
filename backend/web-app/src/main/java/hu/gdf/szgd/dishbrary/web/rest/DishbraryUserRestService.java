package hu.gdf.szgd.dishbrary.web.rest;

import hu.gdf.szgd.dishbrary.security.DishbraryUser;
import hu.gdf.szgd.dishbrary.security.SecurityUtils;
import hu.gdf.szgd.dishbrary.service.UserService;
import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import hu.gdf.szgd.dishbrary.web.exception.UserAlreadyExistsException;
import hu.gdf.szgd.dishbrary.web.model.DishbraryResponse;
import hu.gdf.szgd.dishbrary.web.model.FileResource;
import lombok.extern.log4j.Log4j2;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static hu.gdf.szgd.dishbrary.web.WebConstants.JSON_WITH_UTF8_ENCODING;

@Service
@Path("/user")
@Log4j2
@Produces(JSON_WITH_UTF8_ENCODING)
@Consumes(JSON_WITH_UTF8_ENCODING)
public class DishbraryUserRestService {

	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@POST
	@Path("/login")
	public Response login(DishbraryUser user) {
		try {
			DishbraryUser authenticatedUser = userService.performLogin(user.getUsername(), user.getPassword());

			return Response.ok(
					new DishbraryResponse<>(authenticatedUser)
			).build();
		} catch (InternalAuthenticationServiceException | BadCredentialsException authEx) {
			log.warn("Error during logging in user: {} - Error: {}", user.getUsername(), authEx.getMessage());
			DishbraryResponse response = new DishbraryResponse();

			response.setStatus(Response.Status.UNAUTHORIZED.getStatusCode());
			response.setError(true);

			StringBuilder message = new StringBuilder("A beléptetés sikertelen!");

			if (authEx.getCause() instanceof AuthenticationCredentialsNotFoundException) {
				message.append(" Helytelen felhasználónév vagy jelszó'");
			}

			response.setMessage(message.toString());

			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(response)
					.build();
		}
	}

	@GET
	@Path("/logout")
	@PreAuthorize("isAuthenticated()")
	public Response logout() {
		userService.performLogoutForCurrentUser();

		return Response.ok(new DishbraryResponse<>("Sikeres kijelentkezés!")).build();
	}

	@POST
	@Path("/register")
	public Response register(DishbraryUser userData) {
		try {
			return Response.ok(
					new DishbraryResponse<>(userService.registerUser(userData))
			).build();
		} catch (UserAlreadyExistsException ex) {
			DishbraryResponse response = new DishbraryResponse();

			response.setError(true);
			response.setMessage("A megadott felhasználónév már foglalt!");

			return Response.status(Response.Status.BAD_REQUEST)
					.entity(response)
					.build();
		}
	}

	@POST
	@Path("/data/save")
	@PreAuthorize("isAuthenticated()")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	public Response saveLoggedInUserData(final FormDataMultiPart multiPart) {

		DishbraryUser currentUser = SecurityUtils.getDishbraryUserFromContext();

		String currentPasswordToValidateRequest = multiPart.getField("currentPassword").getValue();

		if (!passwordEncoder.matches(currentPasswordToValidateRequest, currentUser.getPassword())) {
			throw new DishbraryValidationException("Helytelen jelenlegi jelszó!");
		}

		DishbraryUser userData = new DishbraryUser();
		userData.setUsername(multiPart.getField("username").getValue());
		userData.setFirstName(multiPart.getField("firstName").getValue());
		userData.setLastName(multiPart.getField("lastName").getValue());
		userData.setEmail(multiPart.getField("email").getValue());
		userData.setPassword(multiPart.getField("password").getValue());

		boolean profileImageDeleted = Boolean.valueOf(multiPart.getField("profileImageDeleted").getValue());

		FileResource profileImageResource = null;

		if (!profileImageDeleted) {
			BodyPart profileImageBodyPart = multiPart.getField("profileImageInput");
			String fileName = profileImageBodyPart.getContentDisposition().getFileName();
			if (StringUtils.hasText(fileName)) {
				BodyPartEntity bodyPartEntity = (BodyPartEntity) profileImageBodyPart.getEntity();

				profileImageResource = new FileResource(fileName, bodyPartEntity.getInputStream());
			}
		}

		userService.updateUserData(userData, profileImageResource, profileImageDeleted);

		return Response.ok(
				new DishbraryResponse<>("A adatok sikeresen mentve!")
		).build();
	}


	@GET
	@Path("/data")
	@PreAuthorize("isAuthenticated()")
	public Response getLoggedInUserData() {
		return Response.ok(
				new DishbraryResponse<>(SecurityUtils.getDishbraryUserFromContext())
		).build();
	}

	@GET
	@Path("/authenticated")
	public Response isSessionAuthenticated() {
		return Response.ok(
				new DishbraryResponse<>(SecurityUtils.isSessionAuthenticated())
		).build();
	}
}
