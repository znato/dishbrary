
import messagingService from "./services/messaging/MessagingService";
import {eventType} from "./config/MessageConstants";

class ApplicationState {
    isUserAuthenticated = false;

    constructor() {
        messagingService.subscribe(eventType.userLoggedIn, () => {
            this.isUserAuthenticated = true;
        });

        messagingService.subscribe(eventType.userAuthenticated, () => {
            this.isUserAuthenticated = true;
        });

        messagingService.subscribe(eventType.userLoggedOut, () => {
            this.isUserAuthenticated = false;
        });
    }
}

export default new ApplicationState();