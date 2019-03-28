package hu.gdf.szgd.dishbrary.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;

@Log4j2
public class SecurityUtils {

    public static DishbraryUser getDishbraryUserFromContext() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();

        if (userDetails instanceof DishbraryUser) {
            DishbraryUser dishbraryUser = (DishbraryUser) userDetails;

            log.debug("User found in context! Username: {}", dishbraryUser.getUsername());

            return dishbraryUser;
        }

        log.warn("No user found in context!!!");

        return null;
    }
}
