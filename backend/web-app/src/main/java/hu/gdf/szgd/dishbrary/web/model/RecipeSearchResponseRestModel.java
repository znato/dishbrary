package hu.gdf.szgd.dishbrary.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipeSearchResponseRestModel extends PageableRestModel<RecipeRestModel> {

	private List<RecipeRestModel> alternativeResult;

	public RecipeSearchResponseRestModel(List<RecipeRestModel> elements, long totalElements, int totalPages, List<RecipeRestModel> alternativeResult) {
		super(elements, totalElements, totalPages);
		this.alternativeResult = alternativeResult;
	}

	public RecipeSearchResponseRestModel(List<RecipeRestModel> elements, long totalElements, int totalPages) {
		this(elements, totalElements, totalPages, Collections.EMPTY_LIST);
	}
}
