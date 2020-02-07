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
@Table(name = "dishbrary_role")
@SequenceGenerator(name = AbstractEntity.ID_GENERATOR_NAME, sequenceName = "role_seq", allocationSize = 1)
public class Role extends AbstractEntity {

	@Column(nullable = false, unique = true)
	private String name;
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Right> rights;

}
