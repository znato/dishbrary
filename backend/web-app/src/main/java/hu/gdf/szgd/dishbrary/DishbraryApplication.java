package hu.gdf.szgd.dishbrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class DishbraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DishbraryApplication.class, args);
	}

}

