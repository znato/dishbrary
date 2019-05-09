package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.db.repository.CuisineRepository;
import hu.gdf.szgd.dishbrary.transformer.CuisineTransformer;
import hu.gdf.szgd.dishbrary.web.model.CuisineRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class CuisineService {

	@Autowired
	private CuisineRepository cuisineRepository;
	@Autowired
	private CuisineTransformer cuisineTransformer;

	public List<CuisineRestModel> getAllCuisine() {
		return cuisineTransformer.transformAll(cuisineRepository.findAll());
	}
}
