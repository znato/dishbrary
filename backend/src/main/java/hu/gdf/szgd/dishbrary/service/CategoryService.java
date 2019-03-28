package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.db.repository.CategoryRepository;
import hu.gdf.szgd.dishbrary.transformer.CategoryTransformer;
import hu.gdf.szgd.dishbrary.web.model.CategoryRestModel;
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
