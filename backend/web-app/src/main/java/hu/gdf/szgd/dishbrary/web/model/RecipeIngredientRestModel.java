package hu.gdf.szgd.dishbrary.web.model;

import hu.gdf.szgd.dishbrary.db.entity.RecipeIngredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientRestModel {
	private Long id;
	private RecipeRestModel recipe;
	private IngredientRestModel ingredient;
	private Integer quantity;
	private RecipeIngredient.SelectableUnit selectedUnit;
}
