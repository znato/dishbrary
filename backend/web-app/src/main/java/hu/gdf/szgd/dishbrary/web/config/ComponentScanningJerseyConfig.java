package hu.gdf.szgd.dishbrary.web.config;

import hu.gdf.szgd.dishbrary.web.WebConstants;
import lombok.extern.log4j.Log4j2;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Log4j2
@Component
@ApplicationPath(WebConstants.REST_ENDPOINT_BASE_PATH)
public class ComponentScanningJerseyConfig extends ResourceConfig implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object obj, String string) throws BeansException {
		if (obj.getClass().isAnnotationPresent(Path.class)
				|| (obj.getClass().getSimpleName().contains("EnhancerBySpringCGLIB")
				&& obj.getClass().getSuperclass().isAnnotationPresent(Path.class))) {
			Class<?> annotatedClass = obj.getClass().isAnnotationPresent(Path.class) ? obj.getClass()
					: obj.getClass().getSuperclass();
			log.info("Registered path: {} by {}", annotatedClass.getAnnotation(Path.class).value(), obj.getClass().getName());
			register(obj);
		} else if (obj.getClass().isAnnotationPresent(Provider.class)) {
			log.info("Registering Provider: {}", obj.getClass().getSimpleName());
			register(obj);
		} else if (obj instanceof ContainerRequestFilter || obj instanceof ContainerResponseFilter) {
			log.info("Registering filter: {}", obj.getClass().getSimpleName());
			register(obj);
		}

		return obj;
	}
}
