package hu.gdf.szgd.dishbrary.init;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;

@ConditionalOnProperty(prefix = "dishbrary.init", name = "ingredients", havingValue = "true")
@Configuration
@Log4j2
public class IngredientInitializer {

    private static final String INGREDIENT_INIT_SQL_DIRECTORY = "classpath:/sql/ingredients/*.sql";

    @Autowired
    private DataSource ds;

    @PostConstruct
    private void loadData() {
        try {
            PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

            Resource[] sqlFiles = resourceResolver.getResources(INGREDIENT_INIT_SQL_DIRECTORY);

            for (Resource file : sqlFiles) {
                try (Connection connection = ds.getConnection()){
                    log.info("Executing sql file: {}", file.getFilename());

                    ScriptUtils.executeSqlScript(connection, file);
                }
            }
        } catch (Exception e) {
            log.error("Cannot load ingredient sql files!", e);
            throw new Error("Application failed to start!", e);
        }
    }
}
