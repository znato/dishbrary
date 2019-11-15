import React from 'react';
import {Redirect} from 'react-router'

import withStyles from '@material-ui/core/styles/withStyles';
import FormControl from '@material-ui/core/FormControl/index';
import Input from '@material-ui/core/Input/index';
import InputLabel from '@material-ui/core/InputLabel/index';
import CircularProgress from '@material-ui/core/CircularProgress';
import Chip from '@material-ui/core/Chip';
import Typography from "@material-ui/core/es/Typography";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import AddIcon from '@material-ui/icons/Add';
import Fab from '@material-ui/core/Fab';

import RichTextEditor from 'react-rte/lib/RichTextEditor';

import '../../../css/reactRteRestyle.pure.css';

import SuggestionSelect from './SuggestionSelect';
import IngredientEditorDialog from './IngredientEditorDialog';
import DishbraryNumberFormatInput from "./DishbraryNumberFormatInput";

import DishbraryAlertDialog from "../general/DishbraryAlertDialog";

import categoryService from '../../services/CategoryService';
import cuisineService from '../../services/CuisineService';
import ingredientService from '../../services/IngredientService';
import recipeService from '../../services/RecipeService';

import * as ingredientUnitUtils from '../../services/utils/IngredientUnitUtils';

import {LoadingState, LoadingStateByIndex} from '../../services/constants/LoadingState';
import RecipeImageFileUploader from "./RecipeImageFileUploader";
import RecipeVideoFileUploader from "./RecipeVideoFileUploader";

import {userOwnRecipesPath} from '../../config/ApplicationRoutes';

const styles = theme => ({
    form: {
        width: "80%",
        margin: "0 10% 0 10%",
    },
    sectionTitle: {
        color: "rgba(0, 0, 0, 0.54)",
        padding: 0,
        fontFamily: ["Roboto", "Helvetica", "Arial", "sans-serif"],
        lineHeight: 1
    },
    section: {
        textAlign: "left",
    },
    progress: {
        margin: theme.spacing.unit * 2,
    },
    chip: {
        margin: `${theme.spacing.unit / 2}px ${theme.spacing.unit / 4}px`,
    },
    fab: {
        margin: `${theme.spacing.unit / 2}px ${theme.spacing.unit / 4}px`,
    },
    skipButton: {
        "width": "80%",
        "marginBottom": "1em"
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

const EDITOR_STEP = {
    RECIPE: "RECIPE",
    IMAGE_UPLOAD: "IMAGE_UPLOAD",
    VIDEO_UPLOAD: "VIDEO_UPLOAD",
}

class RecipeEditor extends React.Component {

    constructor(props) {
        super(props);

        //recipeId will be set on save
        this.state = {
            actualStep: EDITOR_STEP.RECIPE,
            recipeId: null,
            recipeName: null,
            preparationTime: null,
            cookTime: null,
            portion: null,
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
            selectedCuisines: [],
            imageFileUploaderData: {
                selectedImages: [],
                selectedCoverImageFileName: null
            },
            alertData: {
                openAlert: false,
                alertDialogTitle: "",
                alertDialogContent: ""
            },
            recipeEditingFinished: false
        }
    }

    componentDidMount() {
        this.fetchSuggestionData();
    }

    openAlertDialog = (title, message) => {
        this.setState(
            {
                alertData: {
                    openAlert: true,
                    alertDialogTitle: title,
                    alertDialogContent: message
                }
            })
    }

    closeAlertDialog = () => {
        this.setState(
            {
                alertData: {
                    openAlert: false,
                    alertDialogTitle: "",
                    alertDialogContent: ""
                }
            })
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

    onEventBasedInputChange = name => event => {
        this.setState({[name]: event.target.value})
    }


    onInstructionValueChange = (instructionValue) => {
        this.setState({instructionValue});
    }

    openIngredientEditorDialog = () => {
        this.setState({ingredientEditorOpened: true})
    }

    onIngredientChange = (ingredientData) => {
        this.setState({
            selectedIngredients: [...this.state.selectedIngredients, ingredientData]
        });
    }

    onIngredientDialogClose = () => {
        this.setState({ingredientEditorOpened: false});
    }

    setActualEditorStep = (step) => () => {
        this.setState({actualStep: step});
    }

    deleteIngredient = ingredientData => () => {
        let ingredientArray = this.state.selectedIngredients;

        for (let i = 0; i < ingredientArray.length; i++) {
            if (ingredientArray[i] === ingredientData) {
                ingredientArray.splice(i, 1);
                break;
            }
        }

        this.setState({
            selectedIngredients: ingredientArray
        })
    }

    saveRecipe = (formSubmitEvent) => {
        formSubmitEvent.preventDefault();

        const {
            recipeName, cookTime, preparationTime,
            instructionValue, selectedCategories,
            selectedCuisines, selectedIngredients,
            portion
        } = this.state;

        const recipe = {
                name: recipeName,
                instruction: instructionValue.toString('html'),
                preparationTimeInMinute: preparationTime,
                cookTimeInMinute: cookTime,
                portion: portion,
                ingredients: selectedIngredients.map((ingredientData) => {
                    const ingredient = ingredientData.ingredient;

                    return {
                        ingredient: {
                            id: ingredient.id,
                            name: ingredient.name,
                            unit: ingredientUnitUtils.convertRenderableUnitToUnit(ingredientData.selectedUnit),

                        },
                        quantity: ingredientData.quantity,
                        selectedUnit: ingredientData.selectedUnit

                    };
                }),
                categories: selectedCategories.map(category => {
                    return {
                        id: category.value,
                        name: category.label
                    };
                }),
                cuisines: selectedCuisines.map(cusine => {
                    return {
                        id: cusine.value,
                        name: cusine.label
                    };
                })
            }
        ;

        recipeService.saveRecipe(recipe)
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.openAlertDialog("Hiba történt!", jsonResponse.message);
                } else {
                    this.setState({
                        recipeId: jsonResponse.content.id,
                        actualStep: EDITOR_STEP.IMAGE_UPLOAD
                    });
                }
            });
    }

    handleImageUploadSuccess = (imageFileUploaderData) => {
        this.setState({
            imageFileUploaderData: imageFileUploaderData,
            actualStep: EDITOR_STEP.VIDEO_UPLOAD
        });
    }

    finishRecipeEditing = () => {
        this.setState({recipeEditingFinished: true});
    }

    render() {
        const {
            recipeName, cookTime, preparationTime, portion,
            instructionValue, categoriesLoading,
            categories, ingredientsLoading,
            ingredients, cuisinesLoading, cuisines,
            ingredientEditorOpened, selectedIngredients, selectedCuisines,
            selectedCategories, recipeId, alertData, actualStep, imageFileUploaderData,
            recipeEditingFinished
        } = this.state;

        const highestLoadingStateIndex = Math.max(categoriesLoading.index, ingredientsLoading.index, cuisinesLoading.index);
        const overallLoadingState = LoadingStateByIndex[highestLoadingStateIndex];

        const readyToSave = recipeName && portion && selectedIngredients.length > 0 && instructionValue.getEditorState().getCurrentContent().hasText();

        const {classes} = this.props;

        if (recipeEditingFinished) {
            return <Redirect to={userOwnRecipesPath}/>
        }

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
                            recipeId !== null && EDITOR_STEP.IMAGE_UPLOAD === actualStep
                                ?
                                <div id="recipeImageFileUploadContainer">
                                    <RecipeImageFileUploader recipeId={recipeId}
                                                             selectedImages={imageFileUploaderData.selectedImages}
                                                             selectedCoverImageFileName={imageFileUploaderData.selectedCoverImageFileName}
                                                             onUploadSuccess={this.handleImageUploadSuccess}/>
                                    <Button
                                        className={classes.skipButton}
                                        fullWidth
                                        variant="outlined"
                                        color="primary"
                                        onClick={this.setActualEditorStep(EDITOR_STEP.RECIPE)}
                                    >
                                        Vissza
                                    </Button>
                                    <Button
                                        className={classes.skipButton}
                                        fullWidth
                                        variant="outlined"
                                        color="primary"
                                        onClick={this.setActualEditorStep(EDITOR_STEP.VIDEO_UPLOAD)}
                                    >
                                        Tovább
                                    </Button>
                                </div>
                                :
                                recipeId !== null && EDITOR_STEP.VIDEO_UPLOAD === actualStep
                                    ?
                                    <div id="recipeVideoFileUploadContainer">
                                        <RecipeVideoFileUploader recipeId={recipeId}
                                                                 onUploadSuccess={this.finishRecipeEditing}/>
                                        <Button
                                            className={classes.skipButton}
                                            fullWidth
                                            variant="outlined"
                                            color="primary"
                                            onClick={this.setActualEditorStep(EDITOR_STEP.IMAGE_UPLOAD)}
                                        >
                                            Vissza
                                        </Button>
                                        <Button
                                            className={classes.skipButton}
                                            fullWidth
                                            variant="outlined"
                                            color="primary"
                                            onClick={this.finishRecipeEditing}
                                        >
                                            Befejez
                                        </Button>
                                    </div>
                                    :
                                    <div id="recipeEditorContainer">
                                        <form className={classes.form} onSubmit={this.saveRecipe} autoComplete="off">
                                            <FormControl margin="normal" required fullWidth>
                                                <InputLabel htmlFor="recipeName">Recept neve:</InputLabel>
                                                <Input id="recipeName" name="recipeName"
                                                       value={recipeName ? recipeName : ""} autoFocus
                                                       onChange={this.onEventBasedInputChange('recipeName')}/>
                                            </FormControl>

                                            <FormControl margin="normal" fullWidth>
                                                <TextField
                                                    label="Előkészítési ido (perc):"
                                                    value={preparationTime}
                                                    onChange={this.onEventBasedInputChange('preparationTime')}
                                                    InputProps={{
                                                        inputComponent: DishbraryNumberFormatInput,
                                                    }}
                                                />
                                            </FormControl>

                                            <FormControl margin="normal" fullWidth>
                                                <TextField
                                                    label="Elkészítési ido (perc):"
                                                    value={cookTime}
                                                    onChange={this.onEventBasedInputChange('cookTime')}
                                                    InputProps={{
                                                        inputComponent: DishbraryNumberFormatInput,
                                                    }}
                                                />
                                            </FormControl>

                                            <FormControl margin="normal" required fullWidth>
                                                <TextField
                                                    label="Adagok száma:*"
                                                    value={portion}
                                                    onChange={this.onEventBasedInputChange('portion')}
                                                    InputProps={{
                                                        inputComponent: DishbraryNumberFormatInput,
                                                    }}
                                                />
                                            </FormControl>

                                            <SuggestionSelect label={"Kategóriák:"}
                                                              placeholder="Válassz kategóriákat"
                                                              suggestions={categories}
                                                              value={selectedCategories}
                                                              multiSelect
                                                              onValueChange={this.handleInputChange('selectedCategories')}/>

                                            <div className={classes.section}>
                                                <Typography className={classes.sectionTitle}>Hozzávalók:*</Typography>
                                                <div>
                                                    {
                                                        selectedIngredients.map((ingredientData, index) => {
                                                            const ingredient = ingredientData.ingredient;
                                                            const chipLabel = ingredientData.quantity + " " + ingredientData.selectedUnit + " " + ingredient.name;

                                                            return <Chip className={classes.chip}
                                                                         key={ingredient.id}
                                                                         label={chipLabel}
                                                                         onDelete={this.deleteIngredient(ingredientData)}/>
                                                        })
                                                    }

                                                    <Fab color="primary" className={classes.fab}
                                                         size={"small"}
                                                         onClick={this.openIngredientEditorDialog}>
                                                        <AddIcon/>
                                                    </Fab>
                                                </div>
                                                <hr/>
                                            </div>

                                            <IngredientEditorDialog dialogOpen={ingredientEditorOpened}
                                                                    onDialogClose={this.onIngredientDialogClose}
                                                                    ingredients={ingredients}
                                                                    onIngredientChange={this.onIngredientChange}/>

                                            <SuggestionSelect label={"Konyha nemzetisége:"}
                                                              placeholder="Válassz országot"
                                                              suggestions={cuisines}
                                                              value={selectedCuisines}
                                                              multiSelect
                                                              onValueChange={this.handleInputChange('selectedCuisines')}/>

                                            <FormControl margin="normal" required fullWidth className={classes.section}>
                                                <div id="recipeInstructionLabel"
                                                     className={classes.sectionTitle}>
                                                    Leírás:*
                                                </div>
                                                <RichTextEditor onChange={this.onInstructionValueChange}
                                                                value={instructionValue}
                                                                toolbarConfig={richTextEditorToolbarConfig}/>
                                            </FormControl>

                                            <Button
                                                disabled={!readyToSave}
                                                type="submit"
                                                fullWidth
                                                variant="contained"
                                                color="primary"
                                            >
                                                Mentés
                                            </Button>
                                        </form>

                                        <DishbraryAlertDialog open={alertData.openAlert}
                                                              dialogTitle={alertData.alertDialogTitle}
                                                              dialogContent={alertData.alertDialogContent}
                                                              onAlertDialogClose={this.closeAlertDialog}/>
                                    </div>
                        )
        );
    }
}

export default withStyles(styles, {withTheme: true})(RecipeEditor);