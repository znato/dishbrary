class MessagingService {

    subscribersPerEvent = {};

    publish = (eventName, content) => {
        if (!(eventName in this.subscribersPerEvent)) {
            return;
        }

        const eventSubscribers = this.subscribersPerEvent[eventName].slice();

        eventSubscribers.forEach(subscriber => {
            subscriber(content);
        })
    }

    subscribe = (eventName, callback) => {
        if (!(eventName in this.subscribersPerEvent)) {
            this.subscribersPerEvent[eventName] = [];
        }

        this.subscribersPerEvent[eventName].push(callback);
    }

    unsubscribe = (eventName, callback) => {
        if (!(eventName in this.subscribersPerEvent)) {
            return;
        }

        var eventSubscribers = this.subscribersPerEvent[eventName];

        for (var i = 0, actualLength = eventSubscribers.length; i < actualLength; i++) {
            if (eventSubscribers[i] === callback) {
                eventSubscribers.splice(i, 1);
                return;
            }
        }
    }
}

export default new MessagingService();