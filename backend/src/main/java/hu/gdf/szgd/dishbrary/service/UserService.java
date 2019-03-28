package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.db.entity.User;
import hu.gdf.szgd.dishbrary.db.repository.UserRepository;
import hu.gdf.szgd.dishbrary.security.AnonymousDishbraryUser;
import hu.gdf.szgd.dishbrary.security.DishbraryUser;
import hu.gdf.szgd.dishbrary.security.SecurityUtils;
import hu.gdf.szgd.dishbrary.transformer.UserTransformer;
import hu.gdf.szgd.dishbrary.web.exception.UserAlreadyExistsException;
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

    public DishbraryUser performLogin(String userName, String password) {
        DishbraryUser dishbraryUser = SecurityUtils.getDishbraryUserFromContext();

        log.debug("User in context: {}", dishbraryUser);

        if (dishbraryUser == null) {
            String msg = "Illegal state! No user found in context!";
            log.error(msg);
            throw new IllegalStateException(msg);
        }

        if (dishbraryUser instanceof AnonymousDishbraryUser) {
            log.debug("User in context require authentication - username: {}", userName);

            UsernamePasswordAuthenticationToken auth =
                    (UsernamePasswordAuthenticationToken) authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

            DishbraryUser loggedInUser = (DishbraryUser) auth.getPrincipal();

            //put the principal object into details to get the SecurityUtils.getDishbraryUserFromContext() working properly
            auth.setDetails(loggedInUser);

            log.debug("User[{}] successfully authenticated", userName);

            SecurityContextHolder.getContext().setAuthentication(auth);

            return loggedInUser;
        } else {
            //user already authenticated, no action required
            log.debug("User[{}] already authenticated", userName);
            return dishbraryUser;
        }
    }

    public void performLogoutForCurrentUser() {
        SecurityContextHolder.getContext().setAuthentication(null);

    }

    public DishbraryUser registerUser(DishbraryUser userData) {
        Optional<User> existingUser = userRepository.findUserByUsername(userData.getUsername());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("A megadott felhasználónév már foglalt!");
        } else {
            userData.setPassword(passwordEncoder.encode(userData.getPassword()));
            User newUser = userRepository.save(userTransformer.transformDishbraryUser(validateUser(userData)));

            return userTransformer.transformUser(newUser);
        }
    }

    private DishbraryUser validateUser(DishbraryUser dishbraryUser) {
        return dishbraryUser;
    }
}
