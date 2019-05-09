package hu.gdf.szgd.dishbrary.db.repository;

import hu.gdf.szgd.dishbrary.db.entity.Cuisine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuisineRepository extends CrudRepository<Cuisine, Long> {
}
