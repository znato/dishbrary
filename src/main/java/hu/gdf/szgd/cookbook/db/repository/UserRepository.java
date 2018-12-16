package hu.gdf.szgd.cookbook.db.repository;

import hu.gdf.szgd.cookbook.db.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
}
