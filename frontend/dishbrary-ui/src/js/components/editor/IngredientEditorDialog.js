import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';
import TextField from '@material-ui/core/TextField';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl/index';
import InputLabel from '@material-ui/core/InputLabel/index';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';

import SuggestionSelect from './SuggestionSelect';
import DishbraryNumberFormatInput from './DishbraryNumberFormatInput';

import * as ingredientUnitUtils from '../../services/utils/IngredientUnitUtils';

const styles = theme => ({
    dialogPaper: {
        height: "520px",
    }
});

const EMPTY_STATE = {
    ingredient: {
        id: null,
        name: null,
        unit: null,
    },
    quantity: 1,
    selectedUnit: null
};

class IngredientEditorDialog extends React.Component {
    constructor(props) {
        super(props);

        this.state = EMPTY_STATE;
    }

    closeDialog = () => {
        //call additional callback if present
        this.setState(EMPTY_STATE);

        if (typeof this.props.onDialogClose === "function") {
            this.props.onDialogClose();
        }
    }

    handleIngredientSave = () => {
        var ingredientData = this.state;

        this.closeDialog();

        if (ingredientData.selectedUnit === null) {
            ingredientData.selectedUnit = ingredientUnitUtils.convertUnitToRenderable(ingredientData.ingredient.unit);
        }

        //call additional callback if present
        if (typeof this.props.onIngredientChange === "function") {
            this.props.onIngredientChange(ingredientData);
        }
    }

    handleIngredientChange = value => {
        this.setState({
            ingredient: {
                id: value.value,
                name: value.label,
                unit: value.unit,
            },
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
        const {dialogOpen, ingredients, classes} = this.props;

        const unitToRender = selectedUnit || ingredientUnitUtils.convertUnitToRenderable(ingredient.unit);

        return (
            <Dialog classes={{paper: classes.dialogPaper}} disableBackdropClick disableEscapeKeyDown open={dialogOpen}
                    onClose={this.closeDialog}>
                <DialogTitle>Hozzávaló hozzáadása:</DialogTitle>
                <DialogContent>
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
                                        {ingredientUnitUtils.getUnitSelectionByUnit(unitToRender)}
                                    </Select>
                                </FormControl>
                            </React.Fragment>
                        )
                        : ""
                    }
                </DialogContent>
                <DialogActions>
                    <Button onClick={this.closeDialog} color="secondary">
                        Mégse
                    </Button>
                    <Button onClick={this.handleIngredientSave} color="primary"
                            disabled={ingredient === EMPTY_STATE.ingredient}>
                        Hozzáad
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

export default withStyles(styles, {withTheme: true})(IngredientEditorDialog);