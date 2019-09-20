import * as DishbraryServerRestClient from "./utils/DishbraryServerRestClient";

const createRecipePath = "recipe/create";

class RecipeService {

    saveRecipe = (recipe) => {
        return DishbraryServerRestClient.put(createRecipePath, recipe);
    }

    saveRecipeImages = (recipeId, formData) => {
        const uploadRecipeImagesPath = `resource/recipe/${recipeId}/image/upload`;
        return DishbraryServerRestClient.postFormData(uploadRecipeImagesPath, formData);
    }
}

export default new RecipeService();