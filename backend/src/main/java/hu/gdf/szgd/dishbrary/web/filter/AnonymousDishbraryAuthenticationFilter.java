package hu.gdf.szgd.dishbrary.web.filter;

import hu.gdf.szgd.dishbrary.security.AnonymousDishbraryUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AnonymousDishbraryAuthenticationFilter extends AnonymousAuthenticationFilter {

	private String key;
	private static final AnonymousDishbraryUser ANONYMOUS_DISHBRARY_USER = new AnonymousDishbraryUser();

	public AnonymousDishbraryAuthenticationFilter(String key) {
		this(key, ANONYMOUS_DISHBRARY_USER.getUsername(), AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
	}

	public AnonymousDishbraryAuthenticationFilter(String key, Object principal, List<GrantedAuthority> authorities) {
		super(key, principal, authorities);
		this.key = key;
	}

	@Override
	protected Authentication createAuthentication(HttpServletRequest request) {
		AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(this.key, getPrincipal(), getAuthorities());
		auth.setDetails(ANONYMOUS_DISHBRARY_USER);
		return auth;
	}
}
