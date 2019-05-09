package hu.gdf.szgd.dishbrary.transformer;

import hu.gdf.szgd.dishbrary.StaticResourceComponentType;
import hu.gdf.szgd.dishbrary.db.entity.Ingredient;
import hu.gdf.szgd.dishbrary.web.model.IngredientRestModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class IngredientTransformer {

    @Autowired
    private GenericReflectionBasedTransformer genericTransformer;

    @Value("${dishbrary.ingredients.images.default.name}")
    private String defaultIngredientImageName;

    public IngredientRestModel transform(Ingredient ingredient) {
        IngredientRestModel restModel = genericTransformer.transform(ingredient, new IngredientRestModel());

        String imgFileName = ingredient.getImageFileName();
        if (!StringUtils.isEmpty(imgFileName)) {
            restModel.setImageUrl(StaticResourceComponentType.INGREDIENT.name() + "/" + imgFileName);
        } else {
            restModel.setImageUrl(StaticResourceComponentType.INGREDIENT.name() + "/" + defaultIngredientImageName);
        }

        return restModel;
    }

    public List<IngredientRestModel> transformAll(Iterable<Ingredient> ingredientEntities) {
        List<IngredientRestModel> retVal = new ArrayList<>();

        ingredientEntities.forEach(ingredient -> retVal.add(transform(ingredient)));

        return retVal;
    }
}
