package hu.gdf.szgd.dishbrary.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DishbraryResponse<T> {

    private int status;
    private T body;
    private boolean error;
    private String message;

    public DishbraryResponse(T body) {
        this.status = 200;
        this.body = body;
    }
}
