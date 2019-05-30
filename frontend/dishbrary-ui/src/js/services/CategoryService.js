import * as DishbraryServerRestClient from "./utils/DishbraryServerRestClient";

const getAllCategoriesPath = "recipe/category/all";

class CategoryService {

    getAllCategories = () => {
        return DishbraryServerRestClient.get(getAllCategoriesPath);
    }
}

export default new CategoryService();