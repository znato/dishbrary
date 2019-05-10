package hu.gdf.szgd.dishbrary.init;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;

@ConditionalOnProperty(prefix = "dishbrary.init", name = "security", havingValue = "true")
@Component
@Log4j2
public class RoleRightInitializer extends AbstractDatabaseInitializer {

	private static final String INIT_SQL_FILES_PATTERN = "classpath:/sql/security/*.sql";

	@Override
	protected Resource[] getSqlResources() throws IOException {
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		return resourceResolver.getResources(INIT_SQL_FILES_PATTERN);
	}

	@Override
	protected Logger getLogger() {
		return log;
	}
}
