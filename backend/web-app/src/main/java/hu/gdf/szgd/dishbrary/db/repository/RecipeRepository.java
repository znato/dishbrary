package hu.gdf.szgd.dishbrary.db.repository;

import hu.gdf.szgd.dishbrary.db.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Long> {

	int DEFAULT_PAGE_SIZE = 10;

	Page<Recipe> findByOwnerId(Long userId, Pageable pageInfo);

	@Query(value = "SELECT min(r.id) FROM Recipe r")
	Long findMinId();

	@Query(value = "SELECT max(r.id) FROM Recipe r")
	Long findMaxId();
}
