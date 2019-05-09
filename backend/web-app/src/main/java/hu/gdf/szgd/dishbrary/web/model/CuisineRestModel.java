package hu.gdf.szgd.dishbrary.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuisineRestModel {
	private Long id;
	private String name;
	private String iconUrl;
}
