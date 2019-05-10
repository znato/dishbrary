package hu.gdf.szgd.dishbrary.db.repository;

import hu.gdf.szgd.dishbrary.db.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends org.springframework.data.repository.Repository<Role, Long> {
	String SIMPLE_USER_ROLE_NAME = "SIMPLE_USER";

	Role findByName(String name);
}
