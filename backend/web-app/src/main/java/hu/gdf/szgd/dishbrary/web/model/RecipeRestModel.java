package hu.gdf.szgd.dishbrary.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeRestModel {
	private Long id;
	private String name;
	private String instruction;
	private List<RecipeIngredientRestModel> ingredients;
	private List<CategoryRestModel> categories;
	private List<CuisineRestModel> cuisines;
	private String tags;
	private String coverImageFileName;
	private List<String> additionalImagesFileNames;
	private String videoFileName;
//	private List<Comment> comments;
	private Long popularityIndex;
	private Integer preparationTimeInMinute;
	private Integer cookTimeInMinute;
}
