package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = AbstractEntity.ID_GENERATOR_NAME, sequenceName = "right_seq")
public class Right extends AbstractEntity {

	@Column(nullable = false, unique = true)
	private String name;
	@ManyToMany(mappedBy = "rights")
	private List<Role> roles;
}
