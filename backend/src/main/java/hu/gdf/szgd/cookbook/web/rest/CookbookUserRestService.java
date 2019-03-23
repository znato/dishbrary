package hu.gdf.szgd.cookbook.web.rest;

import hu.gdf.szgd.cookbook.security.CookbookUser;
import hu.gdf.szgd.cookbook.service.UserService;
import hu.gdf.szgd.cookbook.web.exception.UserAlreadyExistsException;
import hu.gdf.szgd.cookbook.web.model.CookbookResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static hu.gdf.szgd.cookbook.web.WebConstants.JSON_WITH_UTF8_ENCODING;

@Service
@Path("/user")
@Log4j2
@Produces(JSON_WITH_UTF8_ENCODING)
@Consumes(JSON_WITH_UTF8_ENCODING)
public class CookbookUserRestService {

    @Autowired
    private UserService userService;

    @POST
    @Path("/login")
    public Response login(CookbookUser user) {
        try {
            CookbookUser authenticatedUser = userService.performLogin(user.getUsername(), user.getPassword());

            return Response.ok(
                    new CookbookResponse<>(authenticatedUser)
            ).build();
        } catch (InternalAuthenticationServiceException | BadCredentialsException authEx) {
            log.warn("Error during logging in user: {} - Error: {}", user.getUsername(), authEx.getMessage());
            CookbookResponse response = new CookbookResponse();

            response.setError(true);
            response.setMessage("A beléptetés sikertelen!");

            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/logout")
    public Response logout() {
        userService.performLogoutForCurrentUser();

        return Response.ok(new CookbookResponse<>("Sikeres kijelentkezés!")).build();
    }

    @POST
    @Path("/register")
    public Response register(CookbookUser userData) {
        try {
            return Response.ok(
                    new CookbookResponse<>(userService.registerUser(userData))
            ).build();
        } catch (UserAlreadyExistsException ex) {
            CookbookResponse response = new CookbookResponse();

            response.setError(true);
            response.setMessage("A megadott felhasználónév már foglalt!");

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }
}
