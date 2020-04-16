package test.hu.gdf.szgd.dishbrary.web.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import hu.gdf.szgd.dishbrary.security.DishbraryUser;
import hu.gdf.szgd.dishbrary.web.model.*;

public interface ResponseTypeReferences {
	TypeReference<DishbraryCollectionResponse<CategoryRestModel>> ALL_CATEGORY_RESPONSE_TYPE = new TypeReference<DishbraryCollectionResponse<CategoryRestModel>>() {};

	TypeReference<DishbraryCollectionResponse<CuisineRestModel>> ALL_CUISINE_RESPONSE_TYPE = new TypeReference<DishbraryCollectionResponse<CuisineRestModel>>() {};

	TypeReference<DishbraryCollectionResponse<IngredientRestModel>> ALL_INGREDIENT_RESPONSE_TYPE = new TypeReference<DishbraryCollectionResponse<IngredientRestModel>>() {};

	TypeReference<DishbraryResponse<RecipeRestModel>> RECIPE_RESPONSE_TYPE = new TypeReference<DishbraryResponse<RecipeRestModel>>() {};

	TypeReference<DishbraryResponse<PageableRestModel<RecipeRestModel>>> PAGEABLE_RECIPE_RESPONSE_TYPE = new TypeReference<DishbraryResponse<PageableRestModel<RecipeRestModel>>>() {};

	TypeReference<DishbraryResponse<RecipeSearchResponseRestModel>> RECIPE_SEARCH_RESPONSE_TYPE = new TypeReference<DishbraryResponse<RecipeSearchResponseRestModel>>() {};

	TypeReference<DishbraryResponse<DishbraryUser>> DISHBRARY_USER_RESPONSE_TYPE = new TypeReference<DishbraryResponse<DishbraryUser>>() {};

	TypeReference<DishbraryResponse<String>> STRING_RESPONSE_TYPE = new TypeReference<DishbraryResponse<String>>() {};
}
