package hu.gdf.szgd.dishbrary.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(
		uniqueConstraints=
		@UniqueConstraint(columnNames={"user_id", "recipe_id"})
)
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = AbstractEntity.ID_GENERATOR_NAME, sequenceName = "favourite_recipe_seq")
public class FavouriteRecipe extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne
	@JoinColumn(name = "recipe_id")
	private Recipe recipe;

}
