import React from 'react';
import withStyles from '@material-ui/core/styles/withStyles';
import FormControl from '@material-ui/core/FormControl/index';
import Input from '@material-ui/core/Input/index';
import InputLabel from '@material-ui/core/InputLabel/index';

import RichTextEditor from 'react-rte/lib/RichTextEditor';

import SuggestionSelect from './SuggestionSelect';

const styles =  theme => ({
    recipeInstructionLabel: {
        color: "rgba(0, 0, 0, 0.54)",
        padding: 0,
        fontFamily: ["Roboto", "Helvetica", "Arial", "sans-serif"],
        lineHeight: 1
    }
});

const richTextEditorToolbarConfig = {
    display: ['INLINE_STYLE_BUTTONS', 'BLOCK_TYPE_BUTTONS', 'BLOCK_TYPE_DROPDOWN', 'HISTORY_BUTTONS'],
    INLINE_STYLE_BUTTONS: [
        {label: 'Bold', style: 'BOLD'},
        {label: 'Italic', style: 'ITALIC'},
        {label: 'Underline', style: 'UNDERLINE'}
    ],
    BLOCK_TYPE_DROPDOWN: [
        {label: 'Normal', style: 'unstyled'},
        {label: 'Heading Large', style: 'header-one'},
        {label: 'Heading Medium', style: 'header-two'},
        {label: 'Heading Small', style: 'header-three'}
    ],
    BLOCK_TYPE_BUTTONS: [
        {label: 'UL', style: 'unordered-list-item'},
        {label: 'OL', style: 'ordered-list-item'}
    ]
};

const suggestions = [
    { label: 'Suggestion1' },
    { label: 'Suggestion2' },
    { label: 'Suggestion3' },
    { label: 'Suggestion4' },
    { label: 'Suggestion5' },

].map(suggestion => ({
    value: suggestion.label,
    label: suggestion.label,
}));

class RecipeEditor extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            instructionValue: RichTextEditor.createEmptyValue(),
            categories: [],
            ingredients: [],
            cuisines: []
        }
    }

    handleInputChange = name => value => {
        this.setState({
            [name]: value,
        });
    };

    onInstructionValueChange = (instructionValue) => {
        this.setState({instructionValue});
    }

    render() {
        const {instructionValue} = this.state;

        const {classes} = this.props;

        return (
            <form>
                <FormControl margin="normal" required fullWidth>
                    <InputLabel htmlFor="recipeName">Recept neve:</InputLabel>
                    <Input id="recipeName" name="recipeName" autoFocus/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="preparationTime">Előkészítési ido:</InputLabel>
                    <Input id="preparationTime" name="preparationTime"/>
                </FormControl>

                <FormControl margin="normal" fullWidth>
                    <InputLabel htmlFor="cookTime">Elkészítési ido:</InputLabel>
                    <Input id="cookTime" name="cookTime"/>
                </FormControl>

                <SuggestionSelect label={"Kategóriák:"}
                                  placeholder="Válassz kategóriákat"
                                  suggestions={suggestions}
                                  multiSelect
                                  onValueChange={this.handleInputChange('categories')}/>

                <SuggestionSelect label={"Hozzávalók:"}
                                  placeholder="Válassz alapanyagokat"
                                  suggestions={suggestions}
                                  multiSelect
                                  onValueChange={this.handleInputChange('ingredients')}/>

                <SuggestionSelect label={"Konyha nemzetisége:"}
                                  placeholder="Válassz országot"
                                  suggestions={suggestions}
                                  multiSelect
                                  onValueChange={this.handleInputChange('cuisines')}/>

                <FormControl margin="normal" required fullWidth>
                    <div id="recipeInstructionLabel" className={classes.recipeInstructionLabel}>Leírás:</div>
                    <RichTextEditor onChange={this.onInstructionValueChange} value={instructionValue} toolbarConfig={richTextEditorToolbarConfig}/>
                </FormControl>

            </form>
        );
    }
}

export default withStyles(styles, {withTheme: true})(RecipeEditor);