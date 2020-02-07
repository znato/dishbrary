package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.db.criteria.RecipeSearchCriteria;
import hu.gdf.szgd.dishbrary.db.entity.FavouriteRecipe;
import hu.gdf.szgd.dishbrary.db.entity.Recipe;
import hu.gdf.szgd.dishbrary.db.entity.User;
import hu.gdf.szgd.dishbrary.db.repository.FavouriteRecipeRepository;
import hu.gdf.szgd.dishbrary.db.repository.RecipeRepository;
import hu.gdf.szgd.dishbrary.db.repository.UserRepository;
import hu.gdf.szgd.dishbrary.security.SecurityUtils;
import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import hu.gdf.szgd.dishbrary.transformer.RecipeSearchCriteriaTransformer;
import hu.gdf.szgd.dishbrary.transformer.RecipeTransformer;
import hu.gdf.szgd.dishbrary.web.model.PageableRestModel;
import hu.gdf.szgd.dishbrary.web.model.RecipeRestModel;
import hu.gdf.szgd.dishbrary.web.model.RecipeSearchResponseRestModel;
import hu.gdf.szgd.dishbrary.web.model.request.RecipeSearchCriteriaRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static hu.gdf.szgd.dishbrary.transformer.TransformerUtil.TRANSFORMER_CONFIG_FOR_RECIPE_PREVIEW;

@Service
@Log4j2
public class FavouriteRecipeService {

	@Autowired
	private FavouriteRecipeRepository favouriteRecipeRepository;
	@Autowired
	private RecipeRepository recipeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RecipeTransformer recipeTransformer;
	@Autowired
	private RecipeSearchCriteriaTransformer criteriaTransformer;

	public void addRecipeToUserFavourites(Long userId, Long recipeId) {
		Optional<User> optionalUser = userRepository.findById(userId);

		if (!optionalUser.isPresent()) {
			throw new DishbraryValidationException("A megadott felhasználói azonosító nem létezik!");
		}

		Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

		if (!optionalRecipe.isPresent()) {
			throw new DishbraryValidationException("A megadott recept azonosító nem létezik!");
		}

		try {
			favouriteRecipeRepository.save(new FavouriteRecipe(optionalUser.get(), optionalRecipe.get()));
		} catch (DataIntegrityViolationException ex) {
			throw new DishbraryValidationException("A recept már el van mentve a kedvencek közé!");
		}
	}

	@Transactional
	public void removeRecipeFromUserFavourites(Long userId, Long recipeId) {
		favouriteRecipeRepository.deleteByUserIdAndRecipeId(userId, recipeId);
	}

	public RecipeSearchResponseRestModel findFavouriteRecipesByCriteria(RecipeSearchCriteriaRestModel searchCriteria, int pageNumber) {
		Long userId = SecurityUtils.getDishbraryUserFromContext().getId();

		Pageable pageInfo = PageRequest.of(pageNumber, RecipeRepository.DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "modificationDate"));

		RecipeSearchCriteria criteria = criteriaTransformer.transform(searchCriteria);

		Page<Recipe> pageableSearchResult = favouriteRecipeRepository.findFavouriteRecipesForUserBySearchCriteria(userId, criteria, pageInfo);

		List<RecipeRestModel> restModels = new ArrayList<>(pageableSearchResult.getSize());

		for (Recipe recipe : pageableSearchResult) {
			RecipeRestModel model = recipeTransformer.transform(recipe, TRANSFORMER_CONFIG_FOR_RECIPE_PREVIEW);
			model.setLikeable(false);
			model.setFavourite(true);

			restModels.add(model);
		}

		return new RecipeSearchResponseRestModel(restModels, pageableSearchResult.getTotalElements(), pageableSearchResult.getTotalPages());
	}

	public PageableRestModel<RecipeRestModel> findFavouriteRecipesForUser(Long userId, int pageNumber) {
		Page<FavouriteRecipe> userFavouriteRecipesPage = favouriteRecipeRepository.findByUserId(
				userId,
				PageRequest.of(pageNumber, RecipeRepository.DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "creationDate")));

		List<RecipeRestModel> restModels = new ArrayList<>(userFavouriteRecipesPage.getSize());

		for (FavouriteRecipe favouriteRecipe : userFavouriteRecipesPage) {
			RecipeRestModel model = recipeTransformer.transform(favouriteRecipe.getRecipe(), TRANSFORMER_CONFIG_FOR_RECIPE_PREVIEW);
			model.setLikeable(false);
			model.setFavourite(true);

			restModels.add(model);
		}

		return new PageableRestModel<>(restModels, userFavouriteRecipesPage.getTotalElements(), userFavouriteRecipesPage.getTotalPages());
	}
}
