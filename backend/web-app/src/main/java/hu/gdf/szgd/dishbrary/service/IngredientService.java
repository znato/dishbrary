package hu.gdf.szgd.dishbrary.service;

import hu.gdf.szgd.dishbrary.db.repository.IngredientRepository;
import hu.gdf.szgd.dishbrary.transformer.IngredientTransformer;
import hu.gdf.szgd.dishbrary.web.model.IngredientRestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private IngredientTransformer ingredientTransformer;

    public List<IngredientRestModel> getAllIngredient() {
        return ingredientTransformer.transformAll(ingredientRepository.findAll());
    }
}
