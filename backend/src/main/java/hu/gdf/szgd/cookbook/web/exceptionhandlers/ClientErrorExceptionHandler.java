package hu.gdf.szgd.cookbook.web.exceptionhandlers;

import hu.gdf.szgd.cookbook.web.model.CookbookResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Component
@Provider
@Log4j2
public class ClientErrorExceptionHandler implements ExceptionMapper<ClientErrorException> {

    private static final Map<Integer, String> CLIENT_ERROR_STATUS_CODE_MESSAGE_MAP = new HashMap<>(6);
    static {
        CLIENT_ERROR_STATUS_CODE_MESSAGE_MAP.put(Response.Status.BAD_REQUEST.getStatusCode(), "Hibás HTTP kérés! A kérés nem megfelelő formatumú!");
        CLIENT_ERROR_STATUS_CODE_MESSAGE_MAP.put(Response.Status.UNAUTHORIZED.getStatusCode(), "A kért erőforrást kizárólag bejelentkezett felhasznalók tekinthetik meg!");
        CLIENT_ERROR_STATUS_CODE_MESSAGE_MAP.put(Response.Status.FORBIDDEN.getStatusCode(), "Hiányzik a megfelelő jogosultság az erőforrás eléréséhez!");
        CLIENT_ERROR_STATUS_CODE_MESSAGE_MAP.put(Response.Status.NOT_ACCEPTABLE.getStatusCode(), "Hibás HTTP kérés! A kliens a szerver által nem támogatott válaszformatumot igényel!");
        CLIENT_ERROR_STATUS_CODE_MESSAGE_MAP.put(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(), "Hibás HTTP kérés! Az elérési mód nem támogatott!");
        CLIENT_ERROR_STATUS_CODE_MESSAGE_MAP.put(Response.Status.NOT_FOUND.getStatusCode(), "Az igényelt erőforrás nem létezik!");
        CLIENT_ERROR_STATUS_CODE_MESSAGE_MAP.put(Response.Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode(), "Hibás HTTP kérés! A kérés a szerver által nem támogatott üzenetet kíván küldeni!");
    }

    @Override
    public Response toResponse(ClientErrorException clientError) {
        int statusCode = clientError.getResponse().getStatus();

        CookbookResponse response = new CookbookResponse();

        response.setStatus(statusCode);
        response.setError(true);
        response.setMessage(CLIENT_ERROR_STATUS_CODE_MESSAGE_MAP.getOrDefault(statusCode, clientError.getMessage()));

        return Response.status(statusCode)
                .entity(response)
                .build();
    }
}
