package hu.gdf.szgd.dishbrary.init;

import hu.gdf.szgd.dishbrary.db.entity.Category;
import hu.gdf.szgd.dishbrary.db.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class CategoryInitializer {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostConstruct
    public void initCategories() {
        categoryRepository.save(new Category("Reggeli"));
        categoryRepository.save(new Category("Ebéd"));
        categoryRepository.save(new Category("Vacsora"));

        categoryRepository.save(new Category("Egészséges"));
        categoryRepository.save(new Category("Könnyen elkészíthető"));
        categoryRepository.save(new Category("Olcsó"));
        categoryRepository.save(new Category("Gyors"));

        categoryRepository.save(new Category("Előételek"));
        categoryRepository.save(new Category("Levesek"));
        categoryRepository.save(new Category("Saláta"));
        categoryRepository.save(new Category("Csirke"));
        categoryRepository.save(new Category("Marha"));
        categoryRepository.save(new Category("Sertés"));

    }
}
