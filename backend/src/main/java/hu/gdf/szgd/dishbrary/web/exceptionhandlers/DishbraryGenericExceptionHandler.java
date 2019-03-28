package hu.gdf.szgd.dishbrary.web.exceptionhandlers;

import hu.gdf.szgd.dishbrary.web.model.DishbraryResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component
@Provider
@Log4j2
public class DishbraryGenericExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable throwable) {
        log.error("Unexpected error during processing request: ", throwable);

        DishbraryResponse response = new DishbraryResponse();

        response.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        response.setError(true);
        response.setMessage("A rendszer működésében átmeneti hiba lépett fel! Mar dolgozunk a javításán!");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .build();
    }

}
