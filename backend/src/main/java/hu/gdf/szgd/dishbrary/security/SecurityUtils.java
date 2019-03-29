package hu.gdf.szgd.dishbrary.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;

@Log4j2
public class SecurityUtils {

	public static DishbraryUser getDishbraryUserFromContext() {
		//it is safe to cast the userDetails without check thanks to the AnonymousDishbraryAuthenticationFilter
		//which will place an anonym DishbraryUser into the context if it is empty
		return (DishbraryUser) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	public static boolean isSessionAuthenticated() {
		return !(getDishbraryUserFromContext() instanceof AnonymousDishbraryUser);
	}
}
