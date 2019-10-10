
export const isNotEmpty = (array) => {
    return Array.isArray(array) && array.length > 0;
};

export const isEmpty = (array) => {
  return !isNotEmpty(array);
};