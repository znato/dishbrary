package hu.gdf.szgd.dishbrary.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DishbraryUser implements UserDetails {

	private Long id;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	@JsonIgnore
	private boolean expired;
	@JsonIgnore
	private boolean banned;
	@JsonIgnore
	private List<GrantedAuthority> grantedAuthorities;

	public void setGrantedAuthorities(List<GrantedAuthority> grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return !expired;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return !banned;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return !expired && !banned;
	}
}
