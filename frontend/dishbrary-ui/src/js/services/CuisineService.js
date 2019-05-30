import * as DishbraryServerRestClient from "./utils/DishbraryServerRestClient";

const getAllCuisinesPath = "recipe/cuisine/all";

class CuisineService {

    getAllCuisines = () => {
        return DishbraryServerRestClient.get(getAllCuisinesPath);
    }
}

export default new CuisineService();