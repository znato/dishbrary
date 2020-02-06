package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.db.entity.User;
import hu.gdf.szgd.dishbrary.db.repository.RoleRepository;
import hu.gdf.szgd.dishbrary.db.repository.UserRepository;
import hu.gdf.szgd.dishbrary.security.DishbraryUser;
import hu.gdf.szgd.dishbrary.security.SecurityUtils;
import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import hu.gdf.szgd.dishbrary.transformer.UserTransformer;
import hu.gdf.szgd.dishbrary.web.model.FileResource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Log4j2
public class UserService {

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserTransformer userTransformer;
	@Autowired
	private StaticResourceService staticResourceService;

	private static final ExecutorService REGISTER_USER_LOGIN_ACTION_EXECUTOR = Executors.newFixedThreadPool(5);

	@Transactional
	public DishbraryUser performLogin(String userName, String password) {
		DishbraryUser dishbraryUser = SecurityUtils.getDishbraryUserFromContext();

		log.debug("User in context: {}", dishbraryUser);

		if (SecurityUtils.isSessionAuthenticated()) {
			//user already authenticated, no action required
			log.debug("User[{}] already authenticated", userName);
			return dishbraryUser;
		}

		log.debug("User in context require authentication - username: {}", userName);

		UsernamePasswordAuthenticationToken auth =
				(UsernamePasswordAuthenticationToken) authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

		DishbraryUser loggedInUser = (DishbraryUser) auth.getPrincipal();

		//put the principal object into details to get the SecurityUtils.getDishbraryUserFromContext() working properly
		auth.setDetails(loggedInUser);

		log.debug("User[{}] successfully authenticated", userName);

		registerUserLoginAction(loggedInUser);

		SecurityContextHolder.getContext().setAuthentication(auth);

		return loggedInUser;

	}

	public void performLogoutForCurrentUser() {
		SecurityContextHolder.getContext().setAuthentication(null);

	}

	public DishbraryUser registerUser(DishbraryUser userData) {
		validateUserPropertiesUniqueness(userData);

		userData.setPassword(passwordEncoder.encode(userData.getPassword()));

		User newUser = userTransformer.transformDishbraryUser(userData);

		newUser.setRole(roleRepository.findByName(RoleRepository.SIMPLE_USER_ROLE_NAME));

		newUser = userRepository.save(newUser);

		return userTransformer.transformUser(newUser);

	}

	@Transactional
	public void updateUserData(DishbraryUser updatedUser, FileResource profileImageResource, boolean deleteProfileImage) {
		validateUserPropertiesUniqueness(updatedUser);

		DishbraryUser userFromContext = SecurityUtils.getDishbraryUserFromContext();

		User userFromDb = userRepository.findById(userFromContext.getId()).get();

		if (StringUtils.hasText(updatedUser.getPassword())) {
			String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());

			updatedUser.setPassword(encodedPassword);
			//update user data in current session to avoid re-login
			userFromContext.setPassword(encodedPassword);
		}

		User newUserState = userTransformer.transformForUpdate(userFromDb, updatedUser);

		if (profileImageResource != null) {
			staticResourceService.saveUserProfileImageForUser(userFromDb, profileImageResource);

			newUserState.setProfileImageFileName(profileImageResource.getFileName());

			//update user data in current session to avoid re-login
			userFromContext.setProfileImageUrl(UserTransformer.PROFILE_IMG_BASE_URL + userFromContext.getId() + "/" + profileImageResource.getFileName());
		} else if (deleteProfileImage) {
			staticResourceService.removeProfileImageForUser(userFromContext.getId(), userFromDb.getProfileImageFileName());
			newUserState.setProfileImageFileName(null);
			//update user data in current session to avoid re-login
			userFromContext.setProfileImageUrl(null);
		}

		userRepository.save(newUserState);
	}

	private void registerUserLoginAction(DishbraryUser user) {
		Date lastLoginDate = new Date();
		user.setLastLoginDate(lastLoginDate);

		REGISTER_USER_LOGIN_ACTION_EXECUTOR.execute(() -> {
			User dbUser = userRepository.findById(user.getId()).get();
			dbUser.setLastLoginDate(lastLoginDate);
			userRepository.save(dbUser);
		});
	}

	private void validateUserPropertiesUniqueness(DishbraryUser user) {
		Optional<Long> existingUserId = userRepository.findUserIdByUsername(user.getUsername());

		if (existingUserId.isPresent() && !existingUserId.get().equals(user.getId())) {
			throw new DishbraryValidationException("A megadott felhasználónév már foglalt!");
		}

		existingUserId = userRepository.findUserIdByEmail(user.getEmail());

		if (existingUserId.isPresent() && !existingUserId.get().equals(user.getId())) {
			throw new DishbraryValidationException("A megadott email cím már foglalt!");
		}
	}
}
