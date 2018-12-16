package hu.gdf.szgd.cookbook.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Recipe extends AbstractEntity {

    @Column(nullable = false)
    private String name;
    @Column
    private String instruction;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Ingredient> ingredients;
    @Column
    @ManyToMany
    private List<Category> categories;
    @Column
    @ManyToMany
    private List<Cuisine> cuisines;
    @Column
    private String tags;
    @Lob
    @Column
    @Basic(fetch = FetchType.LAZY)
    private Byte[] image;
    @Lob
    @Column
    @Basic(fetch = FetchType.LAZY)
    private Byte[] video;
}
