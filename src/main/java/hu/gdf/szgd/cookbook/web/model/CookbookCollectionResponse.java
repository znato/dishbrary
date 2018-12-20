package hu.gdf.szgd.cookbook.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CookbookCollectionResponse<T> extends CookbookResponse<List<T>> {

    public CookbookCollectionResponse(List<T> body) {
        super(body);
    }
}
