package hu.gdf.szgd.dishbrary.db.entity;

import hu.gdf.szgd.dishbrary.db.converter.RecipeAdditionalInfoJsonConverter;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NamedEntityGraph(name = Recipe.FETCH_INGREDIENTS, attributeNodes = {
		@NamedAttributeNode("ingredients")
})
@SequenceGenerator(name = AbstractEntity.ID_GENERATOR_NAME, sequenceName = "recipe_seq", allocationSize = 1)
public class Recipe extends AbstractEntity {

	public static final String FETCH_INGREDIENTS = "recipe.fetch.ingredients";

	@ManyToOne
	private User owner;
	@Column(nullable = false)
	private String name;
	@Column(columnDefinition = "TEXT", nullable = false)
	private String instruction;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "recipe")
	private List<RecipeIngredient> ingredients;
	@Column
	@ManyToMany
	private List<Category> categories;
	@Column
	@ManyToMany
	private List<Cuisine> cuisines;
	@Column
	private String coverImageFileName;
	@ElementCollection
	private List<String> additionalImagesFileNames;
	@Column
	private String videoFileName;
	@Column
	private Long popularityIndex;
	@Column
	private Long preparationTimeInMillis;
	@Column
	private Long cookTimeInMillis;
	@Column(nullable = false)
	private Integer portion;
	@Column
	@Convert(converter = RecipeAdditionalInfoJsonConverter.class)
	private AdditionalInfo additionalInfo;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class AdditionalInfo {
		private String energyKcal;
		private String protein;
		private String fat;
		private String carbohydrate;
	}
}
