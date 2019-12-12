
export const isNotEmpty = (array) => {
    //at the time of implementation browsers are using FileList which is not an array however it is similar to an array (This might change in future browser implementations)
    return (Array.isArray(array) || array instanceof FileList) && array.length > 0;
};

export const isEmpty = (array) => {
  return !isNotEmpty(array);
};