package hu.gdf.szgd.dishbrary.init;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;

public abstract class AbstractDatabaseInitializer {

	@Autowired
	private DataSource ds;

	@PostConstruct
	private void loadData() {
		new Thread(
				() -> {
					try {
						Resource[] sqlFiles = getSqlResources();

						try (Connection connection = ds.getConnection()) {
							for (Resource file : sqlFiles) {

								getLogger().info("Executing sql file: {}", file.getFilename());

								ScriptUtils.executeSqlScript(connection, file);
							}
						}
					} catch (Exception e) {
						getLogger().error("Cannot load sql files!", e);
						throw new Error("Application failed to start!", e);
					}
				}
		).start();
	}

	protected abstract Resource[] getSqlResources() throws IOException;

	protected abstract Logger getLogger();
}
