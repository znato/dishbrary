package hu.gdf.szgd.dishbrary.web.exceptionhandlers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component
@Provider
@Log4j2
public class AccessDeniedExceptionHandler implements ExceptionMapper<AccessDeniedException> {

	@Autowired
	private ClientErrorExceptionHandler clientErrorExceptionHandler;

	@Override
	public Response toResponse(AccessDeniedException e) {
		return clientErrorExceptionHandler
				.toResponse(
						new ClientErrorException(e.getMessage(), Response.Status.UNAUTHORIZED)
				);
	}
}
