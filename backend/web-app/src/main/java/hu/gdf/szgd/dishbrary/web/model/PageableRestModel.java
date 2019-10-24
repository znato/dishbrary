package hu.gdf.szgd.dishbrary.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageableRestModel<T> {

	private List<T> elements;
	private long totalElements;
	private int totalPages;
}
