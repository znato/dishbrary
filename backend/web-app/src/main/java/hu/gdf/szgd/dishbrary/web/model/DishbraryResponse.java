package hu.gdf.szgd.dishbrary.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DishbraryResponse<T> {

	private int status;
	private T content;
	private boolean error;
	private String message;

	public DishbraryResponse(T content) {
		this.status = 200;
		this.content = content;
	}
}
