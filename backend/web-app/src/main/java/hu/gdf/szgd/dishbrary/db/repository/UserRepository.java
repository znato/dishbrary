package hu.gdf.szgd.dishbrary.db.repository;

import hu.gdf.szgd.dishbrary.db.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findUserByUsername(String username);

	@Query("select u.id from User u inner join u.recipes recipe where recipe.id = :recipeId")
	Long findUserIdByRecipesId(Long recipeId);
}
