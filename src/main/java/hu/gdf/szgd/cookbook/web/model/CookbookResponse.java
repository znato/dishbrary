package hu.gdf.szgd.cookbook.web.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
