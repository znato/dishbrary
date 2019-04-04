package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Ingredient extends AbstractEntity {

	@Column(nullable = false, unique = true)
	private String name;
	@Column
	private Integer energy;
	@Column
	private Integer protein;
	@Column
	private Integer fat;
	@Column
	private Integer carbohydrate;
	@Column
	private String imageFileName;
}
