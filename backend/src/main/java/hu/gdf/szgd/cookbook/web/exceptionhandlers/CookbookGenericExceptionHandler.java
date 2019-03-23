package hu.gdf.szgd.cookbook.web.exceptionhandlers;

import hu.gdf.szgd.cookbook.web.model.CookbookResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component
@Provider
@Log4j2
public class CookbookGenericExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable throwable) {
        log.error("Unexpected error during processing request: ", throwable);

        CookbookResponse response = new CookbookResponse();

        response.setError(true);
        response.setMessage("A rendszer működésében átmeneti hiba lépett fel! Mar dolgozunk a javításán!");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .build();
    }

}
