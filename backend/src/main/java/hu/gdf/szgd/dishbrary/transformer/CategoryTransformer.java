package hu.gdf.szgd.dishbrary.transformer;

import hu.gdf.szgd.dishbrary.db.entity.Category;
import hu.gdf.szgd.dishbrary.web.model.CategoryRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class CategoryTransformer {

    public CategoryRestModel transform(Category category) {
        log.debug("Transform Category with name:{} and id: {}", category.getName(), category.getId());

        return new CategoryRestModel(category.getId(), category.getName());
    }


    public List<CategoryRestModel> transformAll(Iterable<Category> categoryEntities) {
        List<CategoryRestModel> retVal = new ArrayList<>();

        categoryEntities.forEach(category -> retVal.add(transform(category)));

        return retVal;
    }
}
