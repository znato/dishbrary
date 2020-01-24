package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@SequenceGenerator(name = AbstractEntity.ID_GENERATOR_NAME, sequenceName = "recipe_ingredient_seq", allocationSize = 1)
public class RecipeIngredient extends AbstractEntity {

	@ManyToOne
	private Recipe recipe;
	@ManyToOne
	private Ingredient ingredient;
	@Column(nullable = false)
	private Integer quantity;
	@Column
	@Enumerated(EnumType.STRING)
	private SelectableUnit selectedUnit;

	public enum SelectableUnit {
		g(1), dkg(10), kg(1000),
		ml(1), dl(100), l(1000),
		db(1);

		private int multiplier;
		private BigDecimal multiplierBigDecimalValue;

		SelectableUnit(int multiplier) {
			this.multiplier = multiplier;
			this.multiplierBigDecimalValue = new BigDecimal(multiplier);
		}

		public int getMultiplier() {
			return multiplier;
		}

		public BigDecimal getMultiplierBigDecimalValue() {
			return multiplierBigDecimalValue;
		}
	}
}
