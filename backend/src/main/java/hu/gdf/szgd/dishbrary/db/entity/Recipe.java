package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;
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
	@Column
	private String instruction;
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Ingredient> ingredients;
	@Column
	@ManyToMany
	private List<Category> categories;
	@Column
	@ManyToMany
	private List<Cuisine> cuisines;
	@Column
	private String tags;
	@Lob
	@Column
	@Basic(fetch = FetchType.LAZY)
	private Byte[] image;
	@Lob
	@Column
	@Basic(fetch = FetchType.LAZY)
	private Byte[] video;
	@OneToMany(mappedBy = "recipe")
	private List<Comment> comments;
}
