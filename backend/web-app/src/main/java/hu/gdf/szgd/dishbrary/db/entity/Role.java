package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Role extends AbstractEntity {

	@Column(nullable = false, unique = true)
	private String name;
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Right> rights;

}
