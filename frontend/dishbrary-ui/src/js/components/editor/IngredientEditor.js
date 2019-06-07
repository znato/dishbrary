import React from 'react';

import TextField from '@material-ui/core/TextField';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl/index';
import InputLabel from '@material-ui/core/InputLabel/index';

import SuggestionSelect from './SuggestionSelect';
import DishbraryNumberFormatInput from './DishbraryNumberFormatInput';


const GRAM_BASED_UNIT_SELECTION = [
    <MenuItem key="gram" value="g">g</MenuItem>,
    <MenuItem key="dkg" value="dkg">dkg</MenuItem>,
    <MenuItem key="kg" value="kg">kg</MenuItem>
]

const MILLILITRE_BASED_UNIT_SELECTION = [
    <MenuItem key="ml" value="ml">ml</MenuItem>,
    <MenuItem key="dl" value="dl">dl</MenuItem>,
    <MenuItem key="l" value="l">l</MenuItem>
]

const PIECE_UNIT = <MenuItem key="db" value="db">db</MenuItem>;

function getUnitSelectionByUnit(unit) {
    switch (unit) {
        case "GRAM":
        case "g":
        case "dkg":
        case "kg":
            return GRAM_BASED_UNIT_SELECTION;

        case "MILLILITRE":
        case "ml":
        case "dl":
        case "l":
            return MILLILITRE_BASED_UNIT_SELECTION;

        default:
            return PIECE_UNIT;
    }
}

function convertUnitToRenderable(unit) {
    switch (unit) {
        case "GRAM":
            return "g";
        case "MILLILITRE":
            return "ml";
        case "PIECE" :
            return "db";
    }

    return unit;
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
            selectedUnit: null
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

        const unitToRender = selectedUnit || convertUnitToRenderable(ingredient.unit);

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

                            <FormControl>
                                <InputLabel shrink>
                                    Egység:
                                </InputLabel>
                                <Select
                                    value={unitToRender}
                                    onChange={this.handleUnitChange}
                                    name="unit"
                                >
                                    {getUnitSelectionByUnit(unitToRender)}
                                </Select>
                            </FormControl>
                        </React.Fragment>
                    )
                    : ""
                }
            </div>
        );
    }
}

export default IngredientEditor;