package hu.gdf.szgd.cookbook.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Comment extends AbstractEntity {

    @ManyToOne
    private User postedBy;
    @Column
    private String content;
    @ManyToOne
    private Recipe recipe;
    @ManyToOne
    private Comment parent;
    @OneToMany(mappedBy = "parent")
    private List<Comment> childComments;

}
