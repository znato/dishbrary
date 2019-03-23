package hu.gdf.szgd.cookbook.web.filter;

import hu.gdf.szgd.cookbook.security.AnonymousCookbookUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AnonymousCookbookAuthenticationFilter extends AnonymousAuthenticationFilter {

    private String key;
    private static final AnonymousCookbookUser anonymousCookbookUser = new AnonymousCookbookUser();

    public AnonymousCookbookAuthenticationFilter(String key) {
        this(key, anonymousCookbookUser.getUsername(), AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
    }

    public AnonymousCookbookAuthenticationFilter(String key, Object principal, List<GrantedAuthority> authorities) {
        super(key, principal, authorities);
        this.key = key;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(this.key, getPrincipal(), getAuthorities());
        auth.setDetails(anonymousCookbookUser);
        return auth;
    }
}
