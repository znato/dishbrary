package hu.gdf.szgd.dishbrary.security;

import org.springframework.security.core.authority.AuthorityUtils;

public class AnonymousDishbraryUser extends DishbraryUser {

	public AnonymousDishbraryUser() {
		setUsername("Anonymous");
		setGrantedAuthorities(AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
	}
}
