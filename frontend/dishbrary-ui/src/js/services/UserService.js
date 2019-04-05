import * as DishbraryServerRestClient from "./utils/DishbraryServerRestClient";

const loginPath = "user/login";
const logoutPath = "user/logout";

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
}

export default new UserService();