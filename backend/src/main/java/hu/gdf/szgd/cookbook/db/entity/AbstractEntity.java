package hu.gdf.szgd.cookbook.db.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    @CreationTimestamp
    private Date creationDate;
    @Column
    @UpdateTimestamp
    private Date modificationDate;

}
