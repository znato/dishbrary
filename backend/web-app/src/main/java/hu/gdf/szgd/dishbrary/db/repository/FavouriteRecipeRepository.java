package hu.gdf.szgd.dishbrary.db.repository;

import hu.gdf.szgd.dishbrary.db.entity.FavouriteRecipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FavouriteRecipeRepository extends CrudRepository<FavouriteRecipe, Long> {

	Page<FavouriteRecipe> findByUserId(Long userId, Pageable pageInfo);

	@Query("SELECT fr.recipe.id FROM FavouriteRecipe fr where fr.user.id = :userId and fr.recipe.id IN (:recipeIds)")
	Set<Long> findFavouriteRecipeIdsByUserIdAndRecipeIds(Long userId, Set<Long> recipeIds);

	void deleteByUserIdAndRecipeId(Long userId, Long recipeId);
}
