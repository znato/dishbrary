import * as DishbraryServerRestClient from "./utils/DishbraryServerRestClient";

const getAllIngredientsPath = "recipe/ingredient/all";

class IngredientService {

    getAllIngredient = () => {
        return DishbraryServerRestClient.get(getAllIngredientsPath);
    }
}

export default new IngredientService();