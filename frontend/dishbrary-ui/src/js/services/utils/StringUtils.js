
export const isNotBlank = (str) => {
    return str && str.trim() != "";
};

export const isBlank = (str) => {
    return !isNotBlank(str);
};