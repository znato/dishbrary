import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';

import {Paper} from "@material-ui/core";
import Typography from "@material-ui/core/Typography";
import SearchIcon from '@material-ui/icons/Search';
import Fab from '@material-ui/core/Fab';

import DishbraryRecipeCard from "../recipe/DishbraryRecipeCard"
import recipeService from "../../services/RecipeService";
import {LoadingState} from "../../services/constants/LoadingState";
import * as RecipeSearchContext from "../../services/constants/RecipeSearchContext";

import * as ArrayUtils from '../../services/utils/ArrayUtils';
import DishbraryProgress from "../general/DishbraryProgress";
import Pagination from "../general/Pagination";
import SuggestionSelect from "../editor/SuggestionSelect";
import ingredientService from "../../services/IngredientService";

const styles = theme => ({
    root: {
        minHeight: '100vh',
        width: '100%',
        marginTop: theme.spacing.unit * 3,
        overflowX: 'auto',
        textAlign: "center",
    },
    recipeCardContainer: {
        textAlign: "left",
    },
    searchBox: {
        width: '90%',
        margin: 'auto'
    },
    ingredientSelect: {
        width: '90%',
        display: 'inline-block'
    },
    searchTriggerBtn: {
        marginLeft: '10px',
        marginTop: '16px',
        position: 'absolute'
    },
    alternativeRecipesLabel: {
        textAlign: 'center',
        paddingTop: '10px',
        paddingBottom: '10px',
    }
});

class WhatIsInTheFridgeView extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            loadingState: LoadingState.none,
            ingredients: [],
            selectedIngredients: [],
            recipes: [],
            alternativeRecipes: [],
            resultLoaded: false,
            searchCriteria: null,
            errorMessage: null,
            actualPage: 0,
            totalElement: null,
            totalPages: null,
        };
    }

    componentDidMount() {
        this.fetchIngredients();
    }

    fetchRecipesByIngredients = (ingredients, pageNumber) => {
        this.setState({
            loadingState: LoadingState.inProgress,
            resultLoaded: false,
        });

        let searchCriteria = {
            ingredientList: ingredients.map(category => {
                return {
                    id: category.value,
                    name: category.label
                };
            })
        };

        recipeService.searchRecipes(RecipeSearchContext.WHAT_IS_IN_THE_FRIDGE, searchCriteria, pageNumber)
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({
                        loadingState: LoadingState.error,
                        errorMessage: jsonResponse.message
                    });
                } else {
                    this.setState({
                        loadingState: LoadingState.loaded,
                        recipes: jsonResponse.content.elements,
                        alternativeRecipes: jsonResponse.content.alternativeResult,
                        resultLoaded: true,
                        totalElement: jsonResponse.content.totalElements,
                        totalPages: jsonResponse.content.totalPages
                    });
                }
            });
    }

    fetchIngredients = () => {
        ingredientService.getAllIngredient()
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({ingredientsLoading: LoadingState.error});
                } else {
                    this.setState({
                        loadingState: LoadingState.loaded,
                        ingredients: jsonResponse.content.map(ingredient => ({
                            value: ingredient.id,
                            label: ingredient.name
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

    triggerSearch = () => {
        this.fetchRecipesByIngredients(this.state.selectedIngredients);
    }

    changePage = (pageNumber) => {
        this.setState({actualPage: pageNumber});

    }

    render() {
        const {classes} = this.props;
        const {ingredients, selectedIngredients, loadingState, recipes, alternativeRecipes, resultLoaded, totalPages, actualPage, errorMessage} = this.state;

        let recipeCards = [];

        let recipesEmpty = ArrayUtils.isEmpty(recipes);
        let alternativeRecipesEmpty = ArrayUtils.isEmpty(alternativeRecipes);

        if (!recipesEmpty) {
            recipeCards = recipes.map(recipe => {
                return (<DishbraryRecipeCard key={recipe.id} recipeData={recipe} />)
            })
        } else if (!alternativeRecipesEmpty) {
            recipeCards = alternativeRecipes.map(recipe => {
                return (<DishbraryRecipeCard key={recipe.id} recipeData={recipe} />)
            })
        }

        return (
            <Paper className={classes.root}>
                <Typography component="h1" variant="h5">
                    Mi van a hűtőben?
                </Typography>

                {
                    loadingState === LoadingState.inProgress
                        ?
                        <DishbraryProgress/>
                        :
                        loadingState === LoadingState.none
                            ?
                            ""
                            :
                            loadingState === LoadingState.error
                                ?
                                <Typography>{errorMessage}</Typography>
                                :
                                (
                                    <React.Fragment>
                                        <div id="search-box" className={classes.searchBox}>
                                            <div className={classes.ingredientSelect}>
                                                <SuggestionSelect label={"Hozzávalók:"}
                                                                  suggestions={ingredients}
                                                                  value={selectedIngredients}
                                                                  multiSelect
                                                                  onValueChange={this.handleInputChange('selectedIngredients')}/>
                                            </div>

                                            <Fab color="primary" aria-label="search"
                                                 className={classes.searchTriggerBtn}
                                                 onClick={this.triggerSearch}
                                                 disabled={ArrayUtils.isEmpty(selectedIngredients)}>
                                                <SearchIcon />
                                            </Fab>
                                        </div>

                                        <div id="recipe-card-container" className={classes.recipeCardContainer}>
                                            {
                                                resultLoaded && recipesEmpty && !alternativeRecipesEmpty
                                                    ?
                                                    <div className={classes.alternativeRecipesLabel}>
                                                        <Typography>
                                                            Sajnos nem található a kérésednek megfelelő recept, de itt
                                                            van nehány ami talán érdekelhet:
                                                        </Typography>
                                                    </div>
                                                    :
                                                    resultLoaded && recipesEmpty && alternativeRecipesEmpty
                                                        ?
                                                        <div className={classes.alternativeRecipesLabel}>
                                                            <Typography>
                                                                Sajnos nem található a kérésednek megfelelő recept
                                                            </Typography>
                                                        </div>
                                                        : ""
                                            }

                                            <Pagination totalPages={totalPages} actualPage={actualPage} onPageChange={this.changePage}>
                                                {recipeCards}
                                            </Pagination>
                                        </div>
                                    </React.Fragment>
                                )
                }
            </Paper>
        )
    }
}

export default withStyles(styles)(WhatIsInTheFridgeView);