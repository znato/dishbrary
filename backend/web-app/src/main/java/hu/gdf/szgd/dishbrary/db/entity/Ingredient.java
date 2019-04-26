package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
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
