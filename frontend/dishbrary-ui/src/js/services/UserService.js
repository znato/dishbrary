import * as DishbraryServerRestClient from "./utils/DishbraryServerRestClient";

const loginPath = "user/login";
const logoutPath = "user/logout";
const registrationPath = "user/register";

class UserService {

    login = (username, password) => {
        return DishbraryServerRestClient.post(loginPath, {
            "username": username,
            "password": password
        });
    }

    logout = () => {
        return DishbraryServerRestClient.get(logoutPath);
    }

    register = (userData) => {
        return DishbraryServerRestClient.post(registrationPath, userData);
    }
}

export default new UserService();