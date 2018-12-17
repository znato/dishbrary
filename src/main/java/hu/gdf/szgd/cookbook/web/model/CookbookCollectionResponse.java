package hu.gdf.szgd.cookbook.web.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CookbookCollectionResponse<T> extends CookbookResponse<List<T>> {

    public CookbookCollectionResponse(List<T> body) {
        super(body);
    }
}
