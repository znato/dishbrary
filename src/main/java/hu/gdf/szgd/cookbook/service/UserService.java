package hu.gdf.szgd.cookbook.service;

import hu.gdf.szgd.cookbook.db.entity.User;
import hu.gdf.szgd.cookbook.db.repository.UserRepository;
import hu.gdf.szgd.cookbook.security.AnonymousCookbookUser;
import hu.gdf.szgd.cookbook.security.CookbookUser;
import hu.gdf.szgd.cookbook.security.SecurityUtils;
import hu.gdf.szgd.cookbook.transformer.UserTransformer;
import hu.gdf.szgd.cookbook.web.exception.UserAlreadyExistsException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class UserService {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserTransformer userTransformer;

    public CookbookUser performLogin(String userName, String password) {
        CookbookUser cookbookUser = SecurityUtils.getCookbookUserFromContext();

        log.debug("User in context: {}", cookbookUser);

        if (cookbookUser == null) {
            String msg = "Illegal state! No user found in context!";
            log.error(msg);
            throw new IllegalStateException(msg);
        }

        if (cookbookUser instanceof AnonymousCookbookUser) {
            log.debug("User in context require authentication - username: {}", userName);

            UsernamePasswordAuthenticationToken auth =
                    (UsernamePasswordAuthenticationToken) authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

            CookbookUser loggedInUser = (CookbookUser) auth.getPrincipal();

            //put the principal object into details to get the SecurityUtils.getCookbookUserFromContext() working properly
            auth.setDetails(loggedInUser);

            log.debug("User[{}] successfully authenticated", userName);

            SecurityContextHolder.getContext().setAuthentication(auth);

            return userTransformer.transformCookbookUserToPresentation(loggedInUser);
        } else {
            //user already authenticated, no action required
            log.debug("User[{}] already authenticated", userName);
            return cookbookUser;
        }
    }

    public void performLogoutForCurrentUser() {
        SecurityContextHolder.getContext().setAuthentication(null);

    }

    public CookbookUser registerUser(CookbookUser userData) {
        Optional<User> existingUser = userRepository.findUserByUsername(userData.getUsername());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("A megadott felhasználónév már foglalt!");
        } else {
            userData.setPassword(passwordEncoder.encode(userData.getPassword()));
            User newUser = userRepository.save(userTransformer.transformCookbookUser(validateUser(userData)));

            return userTransformer.transformUserToPresentation(newUser);
        }
    }

    private CookbookUser validateUser(CookbookUser cookbookUser) {
        return cookbookUser;
    }
}
