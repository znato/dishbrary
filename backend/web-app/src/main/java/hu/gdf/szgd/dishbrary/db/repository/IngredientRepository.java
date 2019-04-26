package hu.gdf.szgd.dishbrary.db.repository;

import hu.gdf.szgd.dishbrary.db.entity.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
}
