package hu.gdf.szgd.dishbrary.init;

import hu.gdf.szgd.dishbrary.db.repository.CategoryRepository;
import hu.gdf.szgd.dishbrary.db.repository.CuisineRepository;
import hu.gdf.szgd.dishbrary.db.repository.IngredientRepository;
import hu.gdf.szgd.dishbrary.db.repository.RoleRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

@ConditionalOnProperty(prefix = "dishbrary.init", name = "generateMockData", havingValue = "true")
@Configuration
@Log4j2
public class MockDataInitializer extends AbstractDatabaseInitializer {

	private static final String INIT_SQL_FILES_PATTERN = "classpath:/sql/mock_data/*.sql";

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private CuisineRepository cuisineRepository;
	@Autowired
	private IngredientRepository ingredientRepository;

	@Override
	protected Resource[] getSqlResources() throws IOException {
		//in order to surely be able to initialize test data check if db is not empty and wait a bit until it is initialized
		if (!isDbProperlyInitialized()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.error("", e);
			}
		}

		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		return resourceResolver.getResources(INIT_SQL_FILES_PATTERN);
	}

	@Override
	protected Logger getLogger() {
		return log;
	}

	private boolean isDbProperlyInitialized() {
		return roleRepository.count() > 0 &&
				categoryRepository.count() > 0 &&
				cuisineRepository.count() > 0 &&
				ingredientRepository.count() > 1000;
	}
}
