package hu.gdf.szgd.dishbrary.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Getter
@Setter
public class Cuisine extends AbstractEntity {

    @Column
    private String name;
    @Lob
    @Column
    private Byte[] icon;

}
