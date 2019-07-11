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

    private static final String BASE_URL = StaticResourceComponentType.INGREDIENT.name() + "/";

    @Autowired
    private GenericReflectionBasedTransformer genericTransformer;

    @Value("${dishbrary.ingredients.images.default.name}")
    private String defaultIngredientImageName;

    public Ingredient transform(IngredientRestModel restModel) {
        Ingredient ingredient = genericTransformer.transform(restModel, new Ingredient());

        if (restModel.getImageUrl() != null && !restModel.getImageUrl().contains(defaultIngredientImageName)) {
            ingredient.setImageFileName(restModel.getImageUrl().replace(BASE_URL, ""));
        }

        return ingredient;
    }

    public List<Ingredient> transformAllIngredientRestModel(Iterable<IngredientRestModel> restModels) {
        List<Ingredient> retVal = new ArrayList<>();

        restModels.forEach(restModel -> retVal.add(transform(restModel)));

        return retVal;
    }

    public IngredientRestModel transform(Ingredient ingredient) {
        IngredientRestModel restModel = genericTransformer.transform(ingredient, new IngredientRestModel());

        String imgFileName = ingredient.getImageFileName();
        if (!StringUtils.isEmpty(imgFileName)) {
            restModel.setImageUrl(BASE_URL + imgFileName);
        } else {
            restModel.setImageUrl(BASE_URL + defaultIngredientImageName);
        }

        return restModel;
    }

    public List<IngredientRestModel> transformAll(Iterable<Ingredient> ingredientEntities) {
        List<IngredientRestModel> retVal = new ArrayList<>();

        ingredientEntities.forEach(ingredient -> retVal.add(transform(ingredient)));

        return retVal;
    }
}
