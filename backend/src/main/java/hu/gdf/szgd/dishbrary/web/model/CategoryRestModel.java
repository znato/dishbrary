package hu.gdf.szgd.dishbrary.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRestModel {
    private Long id;
    private String name;
}
