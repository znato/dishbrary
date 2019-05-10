package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Right extends AbstractEntity {

	@Column(nullable = false, unique = true)
	private String name;
	@ManyToMany(mappedBy = "rights")
	private List<Role> roles;
}
