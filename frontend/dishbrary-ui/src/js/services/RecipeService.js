import * as DishbraryServerRestClient from "./utils/DishbraryServerRestClient";

const createRecipePath = "recipe/create";

class RecipeService {

    saveRecipe = (recipe) => {
        return DishbraryServerRestClient.put(createRecipePath, recipe);
    }
}

export default new RecipeService();