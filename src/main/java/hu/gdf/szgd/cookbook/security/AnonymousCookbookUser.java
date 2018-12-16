package hu.gdf.szgd.cookbook.security;

import org.springframework.security.core.authority.AuthorityUtils;

public class AnonymousCookbookUser extends CookbookUser {

    public AnonymousCookbookUser() {
        setUsername("Anonymous");
        setGrantedAuthorities(AuthorityUtils.createAuthorityList(new String[]{"ROLE_ANONYMOUS"}));
    }
}
