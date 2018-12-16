package hu.gdf.szgd.cookbook.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Category extends AbstractEntity {

    @Column
    private String name;
}
