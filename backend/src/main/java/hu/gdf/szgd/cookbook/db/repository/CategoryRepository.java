package hu.gdf.szgd.cookbook.db.repository;

import hu.gdf.szgd.cookbook.db.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
}
