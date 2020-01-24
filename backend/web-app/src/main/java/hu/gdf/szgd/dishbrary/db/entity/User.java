package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@SequenceGenerator(name = AbstractEntity.ID_GENERATOR_NAME, sequenceName = "user_seq", allocationSize = 1)
public class User extends AbstractEntity {

	@Column
	private String username;
	@Column
	private String profileImageFileName;
	@Column
	private String firstName;
	@Column
	private String lastName;
	@Column
	private String email;
	@Column
	private String password;
	@Column
	private boolean expired;
	@Column
	@Temporal(TemporalType.DATE)
	private Date lastLoginDate;
	@Column
	private boolean banned;
	@ManyToOne(fetch = FetchType.LAZY)
	private Role role;
	@OneToMany(mappedBy = "owner")
	private List<Recipe> recipes;
}
