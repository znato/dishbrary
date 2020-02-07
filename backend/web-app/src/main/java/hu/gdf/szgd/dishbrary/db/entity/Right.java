package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "dishbrary_right")
@SequenceGenerator(name = AbstractEntity.ID_GENERATOR_NAME, sequenceName = "right_seq", allocationSize = 1)
public class Right extends AbstractEntity {

	@Column(nullable = false, unique = true)
	private String name;
	@ManyToMany(mappedBy = "rights")
	private List<Role> roles;
}
