package hu.gdf.szgd.dishbrary.db.repository;

import hu.gdf.szgd.dishbrary.db.criteria.RecipeSearchCriteria;
import hu.gdf.szgd.dishbrary.db.entity.FavouriteRecipe;
import hu.gdf.szgd.dishbrary.db.entity.Recipe;
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
			"and fr.recipe.id in " +
				"(select distinct r.id from Recipe r inner join r.ingredients recipe_ingredient left join r.categories category left join r.cuisines cuisine where " +
					"(" +
						":#{#criteria.plainTextEmpty} = true " +
						"or (" +
							"upper(r.name) like %:#{#criteria.plainTextSearch}% " + //criteria.plainTextSearch is converted t uppercase already by the transformer
							"or upper(r.instruction) like %:#{#criteria.plainTextSearch}% " +
						")" +
					") " +
					"and (" +
						":#{#criteria.categoriesEmpty} = true " +
						"or (category.id in (:#{#criteria.categoryIdList}))" +
					") " +
					"and (" +
						":#{#criteria.cuisinesEmpty} = true " +
						"or (cuisine.id in (:#{#criteria.cuisineIdList}))" +
					") " +
					"and (" +
						":#{#criteria.ingredientsEmpty} = true " +
						"or (recipe_ingredient.ingredient.id in (:#{#criteria.ingredientIdList}))" +
					")" +
				")"
	)
	Page<Recipe> findFavouriteRecipesForUserBySearchCriteria(Long userId, @Param("criteria") RecipeSearchCriteria criteria, Pageable pageInfo);

	void deleteByUserIdAndRecipeId(Long userId, Long recipeId);

	void deleteByRecipeId(Long recipeId);
}
