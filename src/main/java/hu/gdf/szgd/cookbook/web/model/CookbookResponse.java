package hu.gdf.szgd.cookbook.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CookbookResponse<T> {

    private int status;
    private T body;
    private boolean error;
    private String message;

    public CookbookResponse(T body) {
        this.status = 200;
        this.body = body;
    }
}
