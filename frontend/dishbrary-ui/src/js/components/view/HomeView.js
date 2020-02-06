import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';

import {Paper} from "@material-ui/core";
import Typography from "@material-ui/core/Typography";

import DishbraryRecipeCard from "../recipe/DishbraryRecipeCard"
import recipeService from "../../services/RecipeService";
import {LoadingState} from "../../services/constants/LoadingState";
import * as RecipeSearchContext from "../../services/constants/RecipeSearchContext";

import * as ArrayUtils from '../../services/utils/ArrayUtils';
import DishbraryProgress from "../general/DishbraryProgress";
import Pagination from "../general/Pagination";
import DishbraryRecipeSearch from "../recipe/DishbraryRecipeSearch";
import messagingService from "../../services/messaging/MessagingService";
import {eventType} from "../../config/MessageConstants";

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
    }
});

class HomeView extends React.Component {
    isMounted = false;

    constructor(props) {
        super(props);

        this.state = {
            loadingState: LoadingState.none,
            recipes: [],
            searchCriteria: null,
            errorMessage: null,
            actualPage: 0,
            totalElement: null,
            totalPages: null,
        };

        messagingService.subscribe(eventType.userLoggedOut, () => {
            if (this.isMounted) {
                //avoid component update in case it is not mounted
                this.forceUpdate();
            }
        });
    }

    componentDidMount() {
        this.isMounted = true;

        this.fetchRandomRecipes();
    }

    componentWillUnmount() {
        this.isMounted = false;
    }

    fetchRandomRecipes = () => {
        this.setState({
            loadingState: LoadingState.inProgress,
        });

        recipeService.fetchRandomRecipes()
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
                        totalElement: jsonResponse.content.totalElements,
                        totalPages: jsonResponse.content.totalPages
                    });
                }
            });
    };

    fetchRecipesByCriteria = (searchCriteria, pageNumber) => {
        this.setState({
            loadingState: LoadingState.inProgress,
        });

        recipeService.searchRecipes(RecipeSearchContext.ALL_RECIPE, searchCriteria, pageNumber)
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
                        totalElement: jsonResponse.content.totalElements,
                        totalPages: jsonResponse.content.totalPages
                    });
                }
            });
    }

    triggerSearch = (searchCriteria) => {
        this.setState({searchCriteria});

        this.fetchRecipesByCriteria(searchCriteria);
    }

    changePage = (pageNumber) => {
        this.setState({actualPage: pageNumber});

        this.fetchRecipesByCriteria(this.state.searchCriteria, pageNumber);
    }

    render() {
        const {classes} = this.props;
        const {searchCriteria, loadingState, recipes, totalPages, actualPage, errorMessage} = this.state;

        let recipeCards = [];

        let noRecipesFound = loadingState === LoadingState.loaded && ArrayUtils.isEmpty(recipes);

        if (ArrayUtils.isNotEmpty(recipes)) {
            recipeCards = recipes.map(recipe => {
                return (<DishbraryRecipeCard key={recipe.id} recipeData={recipe} onDeleteSuccess={this.fetchUserRecipes}/>)
            })
        }

        return (
            <Paper className={classes.root}>
                <Typography component="h1" variant="h5">
                    Home
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
                                        <DishbraryRecipeSearch searchCriteria={searchCriteria} onSearchTrigger={this.triggerSearch}/>

                                        {
                                            noRecipesFound
                                                ?
                                                <Typography>
                                                    Nem található a keresésnek megfelelő recept
                                                </Typography>
                                                : ""
                                        }

                                        <div id="recipe-card-container" className={classes.recipeCardContainer}>
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

export default withStyles(styles)(HomeView);