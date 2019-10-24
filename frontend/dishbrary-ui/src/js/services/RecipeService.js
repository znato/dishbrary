import * as DishbraryServerRestClient from "./utils/DishbraryServerRestClient";

const createRecipePath = "recipe/create";
const getMyRecipesPath = "recipe/my-recipes"

class RecipeService {

    saveRecipe = (recipe) => {
        return DishbraryServerRestClient.put(createRecipePath, recipe);
    }

    saveRecipeImages = (recipeId, formData) => {
        const uploadRecipeImagesPath = `resource/recipe/${recipeId}/image/upload`;
        return DishbraryServerRestClient.postFormData(uploadRecipeImagesPath, formData);
    }

    saveRecipeVideo = (recipeId, formData) => {
        const uploadRecipeVideoPath = `resource/recipe/${recipeId}/video/upload`;
        return DishbraryServerRestClient.postFormData(uploadRecipeVideoPath, formData);
    }

    fetchLoggedInUserPageableRecipes = (pageNumber) => {
        let fetchUrl = getMyRecipesPath;

        if (pageNumber) {
            fetchUrl += "?" + pageNumber;
        }

        return DishbraryServerRestClient.get(fetchUrl);
    }

    getRecipeImagePath = (recipeId, imageName) => {
        return `rest/resource/image/recipe/${recipeId}/${imageName}`;
    }
}

export default new RecipeService();