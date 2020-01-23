package hu.gdf.szgd.dishbrary.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = AbstractEntity.ID_GENERATOR_NAME, sequenceName = "category_seq")
public class Category extends AbstractEntity {

	@Column
	private String name;
}
