package hu.gdf.szgd.cookbook.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;

@Log4j2
public class SecurityUtils {

    public static CookbookUser getCookbookUserFromContext() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();

        if (userDetails instanceof CookbookUser) {
            CookbookUser cbUser = (CookbookUser) userDetails;

            log.debug("User found in context! Username: {}", cbUser.getUsername());

            return cbUser;
        }

        log.warn("No user found in context!!!");

        return null;
    }
}
