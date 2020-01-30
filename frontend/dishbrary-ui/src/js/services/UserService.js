import * as DishbraryServerRestClient from "./utils/DishbraryServerRestClient";

const loginPath = "user/login";
const logoutPath = "user/logout";
const registrationPath = "user/register";
const isAuthenticatedPath = "user/authenticated";

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

    isUserAuthenticated = () => {
        return DishbraryServerRestClient.get(isAuthenticatedPath);
    }
}

export default new UserService();