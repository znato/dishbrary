package hu.gdf.szgd.dishbrary.security.aop;

import hu.gdf.szgd.dishbrary.db.repository.UserRepository;
import hu.gdf.szgd.dishbrary.security.DishbraryUser;
import hu.gdf.szgd.dishbrary.security.SecurityUtils;
import hu.gdf.szgd.dishbrary.security.annotation.RecipeId;
import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
public class ValidateRecipeBelongsToLoggedInUserAspect {

	@Autowired
	private UserRepository userRepository;

	@Before("@annotation(hu.gdf.szgd.dishbrary.security.annotation.ValidateRecipeBelongsToLoggedInUser)")
	public void validateUser(JoinPoint jp) {
		if (!SecurityUtils.isSessionAuthenticated()) {
			throw new ClientErrorException(Response.Status.UNAUTHORIZED);
		}

		DishbraryUser user = SecurityUtils.getDishbraryUserFromContext();

		Long recipeId = getRecipeIdFromArguments(jp);

		Long userId = userRepository.findUserIdByRecipesId(recipeId);

		if (userId == null) {
			throw new DishbraryValidationException("Nem található recept a megadott ayonosító alatt: " + recipeId);
		}

		if (!user.getId().equals(userId)) {
			throw new DishbraryValidationException("A művelet nem hajtható végre, mert a recept nem hozzád tartozik!");
		}
	}

	private Long getRecipeIdFromArguments(JoinPoint jp) {
		Method method = MethodSignature.class.cast(jp.getSignature()).getMethod();

		Object[] methodArgs = jp.getArgs();

		if (methodArgs == null || methodArgs.length < 1) {
			throw new IllegalArgumentException("Method[" + method.getName() + "]"
					+ " is annotated with @UseRequestContext but there are no arguments. To use this annotation recipeId must be in the parameter list!");
		}

		Annotation[][] annotations = method.getParameterAnnotations();

		Long recipeId = null;

		boolean recipeIdAnnotationFound = false;
		for (int i = 0; i < methodArgs.length; i++) {
			for (Annotation annotation : annotations[i]) {
				if (! (annotation instanceof RecipeId)) {
					continue;
				}

				Object recipeIdParam = methodArgs[i];

				if (! (recipeIdParam instanceof Long)) {
					throw new IllegalArgumentException("recipeId has been found in the method[" + method.getName() + "] parameter list but it must be an instance of Long!");
				}

				recipeId = (Long) recipeIdParam;

				recipeIdAnnotationFound = true;
				break;
			}

			if (recipeIdAnnotationFound) {
				break;
			}
		}

		if (!recipeIdAnnotationFound) {
			throw new IllegalArgumentException("Method[" + method.getName() + "]"
					+ " is annotated with @UseRequestContext. "
					+ "To use this annotation recipeId must be in the parameter list and must be annotated with @RecipeId");
		}

		return recipeId;
	}
}
