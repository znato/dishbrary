
//application intended to be bundled together with the backend. Nevertheless, this can change for scaling reason
//and also for development (to speed up FE development running one server and using node js for FE hosting)
const sameOrigin = true;

export const serverProtocol = "http";
export const serverHost = "localhost";
export const serverPort = "8080";

export const restContextPath = "rest";

export const serverLocation = (() => {
    let serverLocation = "";

    if (!sameOrigin) {
        serverLocation = serverProtocol && serverProtocol + "://";
        serverLocation += serverHost;
        serverLocation += serverPort && ":" + serverPort;
    }

    return serverLocation + "/";
})();

export const serverBaseRestUrl = serverLocation + restContextPath + "/";