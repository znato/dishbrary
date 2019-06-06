import React from 'react';

import TextField from '@material-ui/core/TextField';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl/index';
import InputLabel from '@material-ui/core/InputLabel/index';

import SuggestionSelect from './SuggestionSelect';
import DishbraryNumberFormatInput from './DishbraryNumberFormatInput';

function GramBasedUnitSelection() {
    return (
        [
            <MenuItem key="gram" value="g">g</MenuItem>,
            <MenuItem key="dkg" value="dkg">dkg</MenuItem>,
            <MenuItem key="kg" value="kg">kg</MenuItem>
        ]
    );
}

function MilliBasedUnitSelection() {
    return (
        [
            <MenuItem key="ml" value="ml">ml</MenuItem>,
            <MenuItem key="dl" value="dl">dl</MenuItem>,
            <MenuItem key="l" value="l">l</MenuItem>
        ]

    );
}

function getUnitSelectionByUnit(unit) {
    if (unit === "GRAM") {
        return GramBasedUnitSelection();
    } else if (unit === "MILLILITRE") {
        return MilliBasedUnitSelection();
    }

    return null;
}

function getUnitByRenderedUnitName(renderedUnit) {
    switch (renderedUnit) {
        case "GRAM":
        case "g":
        case "dkg":
        case "kg":
            return "GRAM";

        case "ml":
        case "dl":
        case "l":
            return "MILLILITRE";

        default:
            return "PIECE";
    }
}

class IngredientEditor extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            ingredient: {
                id: null,
                name: null,
                unit: null,
            },
            quantity: 1,
            selectedUnit: null
        }
    }

    handleIngredientChange = value => {
        this.setState({
            ingredient: value,
        });
    };

    handleQuantityChange = event => {
        this.setState({
            quantity: event.target.value,
        });
    };

    handleUnitChange = event => {
        this.setState({
            selectedUnit: event.target.value,
        });
    }

    render() {
        const {ingredient, quantity, selectedUnit} = this.state;
        const {ingredients} = this.props;

        const unitToRender = selectedUnit || ingredient.unit || null;

        return (
            <div>
                <SuggestionSelect label={"Hozzávalók:"}
                                  placeholder="Válassz alapanyagokat"
                                  suggestions={ingredients}
                                  onValueChange={this.handleIngredientChange}/>

                {unitToRender !== null
                    ?
                    (
                        <React.Fragment>
                            <TextField
                                label="Mennyiség:"
                                value={quantity}
                                onChange={this.handleQuantityChange}
                                InputProps={{
                                    inputComponent: DishbraryNumberFormatInput,
                                }}
                            />

                            {getUnitByRenderedUnitName(unitToRender) !== "PIECE"
                                ?
                                <FormControl>
                                    <InputLabel shrink>
                                        Egység:
                                    </InputLabel>
                                    <Select
                                        value={unitToRender}
                                        onChange={this.handleUnitChange}
                                        name="unit"
                                    >
                                        {getUnitSelectionByUnit(getUnitByRenderedUnitName(unitToRender))}
                                    </Select>
                                </FormControl>
                                :
                                <span>db</span>
                            }
                        </React.Fragment>
                    )
                    : ""
                }
            </div>
        );
    }
}

export default IngredientEditor;