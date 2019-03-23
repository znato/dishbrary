package hu.gdf.szgd.cookbook.service;

import hu.gdf.szgd.cookbook.db.repository.CategoryRepository;
import hu.gdf.szgd.cookbook.transformer.CategoryTransformer;
import hu.gdf.szgd.cookbook.web.model.CategoryRestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryTransformer categoryTransformer;

    public List<CategoryRestModel> getAllCategory() {
        return categoryTransformer.transformAll(categoryRepository.findAll());
    }
}
