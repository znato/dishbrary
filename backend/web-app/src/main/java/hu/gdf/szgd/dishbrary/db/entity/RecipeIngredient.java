package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
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
		g,dkg,kg,
		ml, dl, l,
		db
	}
}
