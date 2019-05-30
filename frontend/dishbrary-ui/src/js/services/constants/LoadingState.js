export const LoadingState = {
    none: {
        index: 0,
        name: "none"
    },
    inProgress: {
        index: 1,
        name: "inProgress"
    },
    loaded: {
        index: 2,
        name: "loaded"
    },
    error: {
        index: 3,
        name: "error"
    }
};

export const LoadingStateByIndex = {
    0: LoadingState.none,
    1: LoadingState.inProgress,
    2: LoadingState.loaded,
    3: LoadingState.error,
}