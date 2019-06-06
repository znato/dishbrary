package hu.gdf.szgd.dishbrary.web.model;

import hu.gdf.szgd.dishbrary.db.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientRestModel {
    private Long id;
    private String name;
    private Integer energyKcal;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carbohydrate;
    private String imageUrl;
    private Ingredient.Unit unit;
}
