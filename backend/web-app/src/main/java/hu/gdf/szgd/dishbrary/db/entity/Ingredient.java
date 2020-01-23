package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@SequenceGenerator(name = AbstractEntity.ID_GENERATOR_NAME, sequenceName = "ingredient_seq")
public class Ingredient extends AbstractEntity {

	@Column(nullable = false, unique = true)
	private String name;
	@Column(name = "ENERGYKCAL")
	private Integer energyKcal;
	@Column
	private BigDecimal protein;
	@Column
	private BigDecimal fat;
	@Column
	private BigDecimal carbohydrate;
	@Column
	private String imageFileName;
	@Column
	@Enumerated(EnumType.STRING)
	private Unit unit;

	public enum Unit {
		GRAM,MILLILITRE,PIECE
	}
}
