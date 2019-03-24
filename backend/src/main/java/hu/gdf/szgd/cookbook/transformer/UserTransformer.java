package hu.gdf.szgd.cookbook.transformer;

import hu.gdf.szgd.cookbook.db.entity.Right;
import hu.gdf.szgd.cookbook.db.entity.Role;
import hu.gdf.szgd.cookbook.db.entity.User;
import hu.gdf.szgd.cookbook.security.CookbookUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Log4j2
public class UserTransformer {

    @Autowired
    private GenericReflectionBasedTransformer genericTransformer;

    public CookbookUser transformUser(User user) {
        log.debug("Transfomr user: id[{}], username[{}]", user.getId(), user.getUsername());

        CookbookUser cookbookUser = genericTransformer.transform(user, new CookbookUser());

        cookbookUser.setGrantedAuthorities(mapRoleForUser(user));

        return cookbookUser;
    }

    public CookbookUser transformCookbookUserToPresentation(CookbookUser cookbookUser) {
        CookbookUser retVal = new CookbookUser();

        retVal.setId(cookbookUser.getId());
        retVal.setUsername(cookbookUser.getUsername());
        retVal.setFirstName(cookbookUser.getFirstName());
        retVal.setLastName(cookbookUser.getLastName());
        retVal.setEmail(cookbookUser.getEmail());

        return retVal;
    }

    public CookbookUser transformUserToPresentation(User user) {
        CookbookUser cookbookUser = new CookbookUser();

        cookbookUser.setId(user.getId());
        cookbookUser.setUsername(user.getUsername());
        cookbookUser.setFirstName(user.getFirstName());
        cookbookUser.setLastName(user.getLastName());
        cookbookUser.setEmail(user.getEmail());

        return cookbookUser;
    }

    public User transformCookbookUser(CookbookUser cookbookUser) {
        return genericTransformer.transform(cookbookUser, new User());
    }

    private List<GrantedAuthority> mapRoleForUser(User user) {
        List<GrantedAuthority> grantedAuthorities;

        Role role = user.getRole();

        if (role != null && !CollectionUtils.isEmpty(role.getRights())) {
            List<Right> rights = role.getRights();
            grantedAuthorities = new ArrayList<>(rights.size() + 1);

            String roleName = "ROLE_" + role.getName();
            log.debug("User[{}] has role: {}", user.getUsername(), roleName);

            grantedAuthorities.add(new SimpleGrantedAuthority(roleName));

            rights.forEach(right -> {
                String rightName = right.getName();
                log.debug("User[{}] has right: {}", user.getUsername(), rightName);

                grantedAuthorities.add(new SimpleGrantedAuthority(rightName));
            });
        } else {
            log.debug("Empty role/right set found for user: {}", user.getUsername());
            grantedAuthorities = Collections.emptyList();
        }

        return grantedAuthorities;
    }
}