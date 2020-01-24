package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Entity
@Getter
@Setter
@SequenceGenerator(name = AbstractEntity.ID_GENERATOR_NAME, sequenceName = "cuisine_seq", allocationSize = 1)
public class Cuisine extends AbstractEntity {

	@Column
	private String name;
	@Column
	private String iconFileName;

}
