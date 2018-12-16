package hu.gdf.szgd.cookbook.db.entity;

import lombok.Getter;
import lombok.Setter;

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
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private List<Right> rights;

}
