package hu.gdf.szgd.dishbrary.web.exceptionhandlers;

import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import hu.gdf.szgd.dishbrary.web.WebConstants;
import hu.gdf.szgd.dishbrary.web.model.DishbraryResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component
@Provider
@Log4j2
public class DishbraryValidationExceptionHandler implements ExceptionMapper<DishbraryValidationException> {
	@Override
	public Response toResponse(DishbraryValidationException e) {
		log.error("ValidationException occurred: ", e);

		DishbraryResponse response = new DishbraryResponse();

		response.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
		response.setError(true);
		response.setMessage(e.getMessage());

		return Response.status(Response.Status.BAD_REQUEST)
				.entity(response)
				.type(WebConstants.JSON_WITH_UTF8_ENCODING)
				.build();
	}
}
