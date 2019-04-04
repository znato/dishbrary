package hu.gdf.szgd.dishbrary.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DishbraryCollectionResponse<T> extends DishbraryResponse<List<T>> {

	public DishbraryCollectionResponse(List<T> body) {
		super(body);
	}
}
