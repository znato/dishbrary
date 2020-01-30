import userService from "./UserService";
import messagingService from "./messaging/MessagingService";
import {eventType} from "../config/MessageConstants";

const userLoggedInCheckIntervalInMs = 1000 * 60 * 5;

function run() {
    userService.isUserAuthenticated().then(jsonResponse => {
        if (!jsonResponse.error && jsonResponse.content) {
            messagingService.publish(eventType.userAuthenticated);
        } else {
            messagingService.publish(eventType.userLoggedOut);
        }
    });

    setTimeout(run, userLoggedInCheckIntervalInMs);
}

run();