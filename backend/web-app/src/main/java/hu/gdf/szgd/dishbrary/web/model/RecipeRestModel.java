package hu.gdf.szgd.dishbrary.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import hu.gdf.szgd.dishbrary.security.DishbraryUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
	private String coverImageFileName;
	private List<String> additionalImagesFileNames;
	private String videoFileName;
	private Integer preparationTimeInMinute;
	private Integer cookTimeInMinute;
	private Integer portion;
	private CalorieInfo calorieInfo;
	private DishbraryUser owner;
	private Date creationDate;
	private boolean editable = false;
	private boolean likeable = false;
	private boolean favourite = false;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class CalorieInfo {
		private String energyKcal;
		private String protein;
		private String fat;
		private String carbohydrate;
	}
}
