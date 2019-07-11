package hu.gdf.szgd.dishbrary.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRestModel {
	private Long id;
	private String name;
	private String instruction;
	private List<RecipeIngredientRestModel> ingredients;
	private List<CategoryRestModel> categories;
	private List<CuisineRestModel> cuisines;
	private String tags;
	private String coverImageFileName;
	private String videoFileName;
//	private List<Comment> comments;
	private Long popularityIndex;
	private Integer preparationTimeInMinute;
	private Integer cookTimeInMinute;
}
