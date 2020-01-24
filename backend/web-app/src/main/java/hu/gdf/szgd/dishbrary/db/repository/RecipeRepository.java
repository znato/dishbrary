package hu.gdf.szgd.dishbrary.db.repository;

import hu.gdf.szgd.dishbrary.db.criteria.RecipeSearchCriteria;
import hu.gdf.szgd.dishbrary.db.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Long> {

	int DEFAULT_PAGE_SIZE = 10;

	@EntityGraph(Recipe.FETCH_INGREDIENTS)
	@Query("select r from Recipe r where r.id = :id")
	Optional<Recipe> findByIdAndFetchIngredients(Long id);

	Page<Recipe> findByOwnerId(Long userId, Pageable pageInfo);


	@Query("select distinct r from Recipe r inner join r.ingredients recipe_ingredient left join r.categories category left join r.cuisines cuisine where " +
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
			")"
	)
	Page<Recipe> findBySearchCriteria(@Param("criteria") RecipeSearchCriteria criteria, Pageable pageInfo);

	@Query("select distinct r from Recipe r inner join r.ingredients recipe_ingredient left join r.categories category left join r.cuisines cuisine where " +
			"r.owner.id = :ownerId " +
			"and (" +
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
			")"
	)
	Page<Recipe> findByOwnerIdAndSearchCriteria(Long ownerId, @Param("criteria") RecipeSearchCriteria criteria, Pageable pageInfo);

	@Query(value = "SELECT min(r.id) FROM Recipe r")
	Long findMinId();

	@Query(value = "SELECT max(r.id) FROM Recipe r")
	Long findMaxId();
}
