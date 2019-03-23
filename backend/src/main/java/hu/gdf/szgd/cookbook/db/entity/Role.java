package hu.gdf.szgd.cookbook.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Role extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "role")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Right> rights;

}
