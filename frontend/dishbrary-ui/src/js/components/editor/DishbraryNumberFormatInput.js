import NumberFormat from 'react-number-format';
import React from "react";

function DishbraryNumberFormatInput(props) {
    const { value, inputRef, onChange, ...other } = props;

    return (
        <NumberFormat
            {...other}
            getInputRef={inputRef}
            value={value}
            onValueChange={values => {
                onChange({
                    target: {
                        value: values.value,
                    },
                });
            }}
            thousandSeparator
        />
    );
}

export default DishbraryNumberFormatInput;