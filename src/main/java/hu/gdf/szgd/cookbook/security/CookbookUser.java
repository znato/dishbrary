package hu.gdf.szgd.cookbook.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class CookbookUser implements UserDetails {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean expired;
    private boolean banned;
    private List<GrantedAuthority> grantedAuthorities;

    public void setGrantedAuthorities(List<GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !banned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return !expired && !banned;
    }
}
