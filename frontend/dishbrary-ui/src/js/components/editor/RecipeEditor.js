import React from 'react';
import withStyles from '@material-ui/core/styles/withStyles';
import FormControl from '@material-ui/core/FormControl/index';
import Input from '@material-ui/core/Input/index';
import InputLabel from '@material-ui/core/InputLabel/index';
import CircularProgress from '@material-ui/core/CircularProgress';
import Chip from '@material-ui/core/Chip';

import RichTextEditor from 'react-rte/lib/RichTextEditor';

import AddIcon from '@material-ui/icons/Add';
import Fab from '@material-ui/core/Fab';
import SuggestionSelect from './SuggestionSelect';
import IngredientEditorDialog from './IngredientEditorDialog';

import categoryService from '../../services/CategoryService';
import cuisineService from '../../services/CuisineService';
import ingredientService from '../../services/IngredientService';

import {LoadingState, LoadingStateByIndex} from '../../services/constants/LoadingState';
import Typography from "@material-ui/core/es/Typography";

const styles = theme => ({
    recipeInstructionLabel: {
        color: "rgba(0, 0, 0, 0.54)",
        padding: 0,
        fontFamily: ["Roboto", "Helvetica", "Arial", "sans-serif"],
        lineHeight: 1
    },
    progress: {
        margin: theme.spacing.unit * 2,
    },
    fab: {
        margin: theme.spacing.unit * 2,
    },
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

class RecipeEditor extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            ingredientEditorOpened: false,
            instructionValue: RichTextEditor.createEmptyValue(),
            categoriesLoading: LoadingState.none,
            categories: [],
            selectedCategories: [],
            ingredientsLoading: LoadingState.none,
            ingredients: [],
            selectedIngredients: [],
            cuisinesLoading: LoadingState.none,
            cuisines: [],
            selectedCuisines: []
        }
    }

    componentDidMount() {
        this.fetchSuggestionData();
    }

    fetchSuggestionData = () => {
        this.setState({
            categoriesLoading: LoadingState.inProgress,
            ingredientsLoading: LoadingState.inProgress,
            cuisinesLoading: LoadingState.inProgress,
        });

        categoryService.getAllCategories()
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({categoriesLoading: LoadingState.error});
                } else {
                    this.setState({
                        categoriesLoading: LoadingState.loaded,
                        categories: jsonResponse.content.map(category => ({
                            value: category.id,
                            label: category.name
                        }))
                    });
                }
            });

        ingredientService.getAllIngredient()
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({ingredientsLoading: LoadingState.error});
                } else {
                    this.setState({
                        ingredientsLoading: LoadingState.loaded,
                        ingredients: jsonResponse.content.map(ingredient => ({
                            value: ingredient.id,
                            label: ingredient.name,
                            unit: ingredient.unit
                        }))
                    });
                }
            });

        cuisineService.getAllCuisines()
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({cuisinesLoading: LoadingState.error});
                } else {
                    this.setState({
                        cuisinesLoading: LoadingState.loaded,
                        cuisines: jsonResponse.content.map(cuisine => ({
                            value: cuisine.id,
                            label: cuisine.name
                        }))
                    });
                }
            });
    }

    handleInputChange = name => value => {
        this.setState({
            [name]: value,
        });
    };

    onInstructionValueChange = (instructionValue) => {
        this.setState({instructionValue});
    }

    openIngredientEditorDialog = () => {
        this.setState({ingredientEditorOpened: true})
    }

    onIngredientChange = (ingredientDescription) => {
        this.setState({
            selectedIngredients: [...this.state.selectedIngredients, ingredientDescription]
        });
    }

    onIngredientDialogClose = () => {
        this.setState({ingredientEditorOpened: false});
    }

    render() {
        const {
            instructionValue, categoriesLoading,
            categories, ingredientsLoading,
            ingredients, cuisinesLoading, cuisines,
            ingredientEditorOpened, selectedIngredients
        } = this.state;

        const highestLoadingStateIndex = Math.max(categoriesLoading.index, ingredientsLoading.index, cuisinesLoading.index);
        const overallLoadingState = LoadingStateByIndex[highestLoadingStateIndex];

        const {classes} = this.props;

        return (
            overallLoadingState === LoadingState.inProgress
                ?
                <CircularProgress disableShrink={true} className={classes.progress}/>
                :
                overallLoadingState === LoadingState.none
                    ?
                    ""
                    :
                    overallLoadingState === LoadingState.error ?
                        <Typography>Az oldal jelenleg nem elérhető! Kérjük próbálja később!</Typography>
                        :
                        (
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
                                                  suggestions={categories}
                                                  multiSelect
                                                  onValueChange={this.handleInputChange('selectedCategories')}/>

                                <div>
                                    <div>
                                        {
                                            selectedIngredients.map((ingredientDescription) => {
                                                const ingredient = ingredientDescription.ingredient;
                                                const chipLabel = ingredientDescription.quantity + " " + ingredientDescription.selectedUnit + " " + ingredient.name;

                                                return <Chip key={ingredient.id} label={chipLabel}/>
                                            })
                                        }
                                    </div>
                                    <Fab color="primary" className={classes.fab}
                                         onClick={this.openIngredientEditorDialog}>
                                        <AddIcon/>
                                    </Fab>
                                </div>

                                <IngredientEditorDialog dialogOpen={ingredientEditorOpened}
                                                        onDialogClose={this.onIngredientDialogClose}
                                                        ingredients={ingredients}
                                                        onIngredientChange={this.onIngredientChange}/>

                                <SuggestionSelect label={"Konyha nemzetisége:"}
                                                  placeholder="Válassz országot"
                                                  suggestions={cuisines}
                                                  multiSelect
                                                  onValueChange={this.handleInputChange('selectedCuisines')}/>

                                <FormControl margin="normal" required fullWidth>
                                    <div id="recipeInstructionLabel"
                                         className={classes.recipeInstructionLabel}>Leírás:
                                    </div>
                                    <RichTextEditor onChange={this.onInstructionValueChange} value={instructionValue}
                                                    toolbarConfig={richTextEditorToolbarConfig}/>
                                </FormControl>

                            </form>
                        )
        );
    }
}

export default withStyles(styles, {withTheme: true})(RecipeEditor);