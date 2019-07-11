package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.db.entity.Recipe;
import hu.gdf.szgd.dishbrary.db.repository.RecipeRepository;
import hu.gdf.szgd.dishbrary.transformer.RecipeTransformer;
import hu.gdf.szgd.dishbrary.web.model.RecipeRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;
	@Autowired
	private RecipeTransformer recipeTransformer;

	public RecipeRestModel createRecipe(RecipeRestModel recipeRestModel) {
		Recipe recipeToSave = recipeTransformer.transform(recipeRestModel);

		//TODO: validate recipe

		return recipeTransformer.transform(recipeRepository.save(recipeToSave));
	}
}
