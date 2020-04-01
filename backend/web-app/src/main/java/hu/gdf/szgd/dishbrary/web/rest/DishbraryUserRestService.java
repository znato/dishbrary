package hu.gdf.szgd.dishbrary.web.rest;

import hu.gdf.szgd.dishbrary.security.DishbraryUser;
import hu.gdf.szgd.dishbrary.security.SecurityUtils;
import hu.gdf.szgd.dishbrary.service.UserService;
import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import hu.gdf.szgd.dishbrary.web.model.DishbraryResponse;
import hu.gdf.szgd.dishbrary.web.model.FileResource;
import hu.gdf.szgd.dishbrary.web.validation.DishbraryUserValidationUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
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
import java.io.IOException;

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
		DishbraryUserValidationUtil.validateUser(userData, true);

		return Response.ok(
				new DishbraryResponse<>(userService.registerUser(userData))
		).build();
	}

	@POST
	@Path("/data/save")
	@PreAuthorize("isAuthenticated()")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	public Response saveLoggedInUserData(@Multipart(value = "currentPassword", required = false) String currentPasswordToValidateRequest,
										 @Multipart(value = "username", required = false) String username,
										 @Multipart(value = "firstName", required = false) String firstName,
										 @Multipart(value = "lastName", required = false) String lastName,
										 @Multipart(value = "email", required = false) String email,
										 @Multipart(value = "password", required = false) String password,
										 @Multipart(value = "profileImageDeleted", required = false) Boolean profileImageDeleted,
										 @Multipart(value = "profileImageInput", required = false) Attachment profileImageInput) {

		DishbraryUser currentUser = SecurityUtils.getDishbraryUserFromContext();

		if (!passwordEncoder.matches(currentPasswordToValidateRequest, currentUser.getPassword())) {
			throw new DishbraryValidationException("Helytelen jelenlegi jelszó!");
		}

		DishbraryUser userData = new DishbraryUser();
		userData.setId(currentUser.getId());
		userData.setUsername(username);
		userData.setFirstName(firstName);
		userData.setLastName(lastName);
		userData.setEmail(email);
		userData.setPassword(password);

		DishbraryUserValidationUtil.validateUser(userData, false);

		FileResource profileImageResource = null;

		if (!profileImageDeleted) {
			String fileName = profileImageInput.getContentDisposition().getFilename();
			if (StringUtils.hasText(fileName)) {
				try {
					profileImageResource = new FileResource(fileName, profileImageInput.getDataHandler().getInputStream());
				} catch (IOException e) {
					log.warn("File data inputstream cannot be obtained!", e);
				}
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
