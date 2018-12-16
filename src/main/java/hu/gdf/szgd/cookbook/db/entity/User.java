package hu.gdf.szgd.cookbook.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class User extends AbstractEntity{

    @Column
    private String username;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String password;
    @Column
    private boolean expired;
    @Column
    private boolean banned;
    @ManyToOne
    private Role role;
}
