export const LoadingState = {
    none: {
        index: 0,
        name: "none"
    },
    loaded: {
        index: 1,
        name: "loaded"
    },
    inProgress: {
        index: 2,
        name: "inProgress"
    },
    error: {
        index: 3,
        name: "error"
    }
};

export const LoadingStateByIndex = {
    0: LoadingState.none,
    1: LoadingState.loaded,
    2: LoadingState.inProgress,
    3: LoadingState.error,
}