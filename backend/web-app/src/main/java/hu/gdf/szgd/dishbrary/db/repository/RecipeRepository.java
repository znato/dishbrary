package hu.gdf.szgd.dishbrary.db.repository;

import hu.gdf.szgd.dishbrary.db.entity.Recipe;
import hu.gdf.szgd.dishbrary.web.model.request.RecipeSearchCriteria;
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


	@Query("select r from Recipe r where " +
			"(" +
				":#{#criteria.plainTextSearch} is null " +
				"or (" +
					"r.name like '%:#{#criteria.plainTextSearch}%' " +
					"or r.instruction like '%:#{#criteria.plainTextSearch}%' " +
				")" +
			") " +
			"and (" +
				":#{#criteria.categoryList} is null " +
				"or (r.categories in (:#{#criteria.categoryList}))" +
			") " +
			"and (" +
				":#{#criteria.cuisineList} is null " +
				"or (r.cuisines in (:#{#criteria.cuisineList}))" +
			") " +
			"and (" +
				":#{#criteria.ingredientList} is null) " +
				"or (r.ingredients in (:#{#criteria.ingredientList})" +
			")"
	)
	Page<Recipe> findBySearchCriteria(@Param("criteria") RecipeSearchCriteria criteria, Pageable pageInfo);

	@Query("select r from Recipe r where " +
			"r.owner.id = :ownerId " +
			"and (" +
				":#{#criteria.plainTextSearch} is null " +
				"or (" +
					"r.name like '%:#{#criteria.plainTextSearch}%' " +
					"or r.instruction like '%:#{#criteria.plainTextSearch}%' " +
				")" +
			") " +
			"and (" +
				":#{#criteria.categoryList} is null " +
				"or (r.categories in (:#{#criteria.categoryList}))" +
			") " +
			"and (" +
				":#{#criteria.cuisineList} is null " +
				"or (r.cuisines in (:#{#criteria.cuisineList}))" +
			") " +
			"and (" +
				":#{#criteria.ingredientList} is null) " +
				"or (r.ingredients in (:#{#criteria.ingredientList})" +
			")"
	)
	Page<Recipe> findByOwnerIdAndSearchCriteria(Long ownerId, @Param("criteria") RecipeSearchCriteria criteria, Pageable pageInfo);

	@Query(value = "SELECT min(r.id) FROM Recipe r")
	Long findMinId();

	@Query(value = "SELECT max(r.id) FROM Recipe r")
	Long findMaxId();
}
