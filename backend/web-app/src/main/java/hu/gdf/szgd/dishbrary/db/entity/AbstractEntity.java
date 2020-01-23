package hu.gdf.szgd.dishbrary.db.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {

	public static final String ID_GENERATOR_NAME = "ID_GENERATOR";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GENERATOR_NAME)
	private Long id;
	@Column
	@CreationTimestamp
	private Date creationDate;
	@Column
	@UpdateTimestamp
	private Date modificationDate;

}
