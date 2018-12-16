package hu.gdf.szgd.cookbook.security;

import hu.gdf.szgd.cookbook.db.entity.User;
import hu.gdf.szgd.cookbook.db.repository.UserRepository;
import hu.gdf.szgd.cookbook.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CookbookUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTransformer userTransformer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userInDb = userRepository.findUserByUsername(username);

        return userInDb.map(user -> userTransformer.transformUser(user))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Invalid username or password!"));
    }
}
