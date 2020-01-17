package hu.gdf.szgd.dishbrary.db.repository;

import hu.gdf.szgd.dishbrary.db.entity.FavouriteRecipe;
import hu.gdf.szgd.dishbrary.db.entity.Recipe;
import hu.gdf.szgd.dishbrary.web.model.request.RecipeSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FavouriteRecipeRepository extends CrudRepository<FavouriteRecipe, Long> {

	Page<FavouriteRecipe> findByUserId(Long userId, Pageable pageInfo);

	@Query("select fr.recipe.id from FavouriteRecipe fr where fr.user.id = :userId and fr.recipe.id in (:recipeIds)")
	Set<Long> findFavouriteRecipeIdsByUserIdAndRecipeIds(Long userId, Set<Long> recipeIds);

	@Query("select fr.recipe from FavouriteRecipe fr where " +
			"fr.user.id = :userId " +
			"and (" +
				":#{#criteria.plainTextSearch} is null " +
				"or (" +
					"fr.recipe.name like '%:#{#criteria.plainTextSearch}%' " +
					"or fr.recipe.instruction like '%:#{#criteria.plainTextSearch}%' " +
				")" +
			") " +
			"and (" +
				":#{#criteria.categoryList} is null " +
				"or (fr.recipe.categories in (:#{#criteria.categoryList}))" +
			") " +
			"and (" +
				":#{#criteria.cuisineList} is null " +
				"or (fr.recipe.cuisines in (:#{#criteria.cuisineList}))" +
			") " +
			"and (" +
				":#{#criteria.ingredientList} is null) " +
				"or (fr.recipe.ingredients in (:#{#criteria.ingredientList})" +
			")"
	)
	Page<Recipe> findFavouriteRecipesForUserBySearchCriteria(Long userId, @Param("criteria") RecipeSearchCriteria criteria, Pageable pageInfo);

	void deleteByUserIdAndRecipeId(Long userId, Long recipeId);
}
