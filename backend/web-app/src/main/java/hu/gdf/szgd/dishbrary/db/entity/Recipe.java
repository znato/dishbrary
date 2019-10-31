package hu.gdf.szgd.dishbrary.db.entity;

import hu.gdf.szgd.dishbrary.db.converter.RecipeAdditionalInfoJsonConverter;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Recipe extends AbstractEntity {

	@ManyToOne
	private User owner;
	@Column(nullable = false)
	private String name;
	@Lob
	@Column
	private String instruction;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", fetch = FetchType.EAGER)
	private List<RecipeIngredient> ingredients;
	@Column
	@ManyToMany
	private List<Category> categories;
	@Column
	@ManyToMany
	private List<Cuisine> cuisines;
	@Column
	private String tags;
	@Column
	private String coverImageFileName;
	@ElementCollection
	private List<String> additionalImagesFileNames;
	@Column
	private String videoFileName;
	@OneToMany(mappedBy = "recipe")
	private List<Comment> comments;
	@Column
	private Long popularityIndex;
	@Column
	private Long preparationTimeInMillis;
	@Column
	private Long cookTimeInMillis;
	@Column
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
