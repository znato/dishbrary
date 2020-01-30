import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';

import {Paper} from "@material-ui/core";
import Typography from "@material-ui/core/Typography";

import DishbraryRecipeCard from "../recipe/DishbraryRecipeCard"
import recipeService from "../../services/RecipeService";
import {LoadingState} from "../../services/constants/LoadingState";

import * as ArrayUtils from '../../services/utils/ArrayUtils';
import DishbraryProgress from "../general/DishbraryProgress";
import Pagination from "../general/Pagination";
import DishbraryRecipeSearch from "../recipe/DishbraryRecipeSearch";
import * as RecipeSearchContext from "../../services/constants/RecipeSearchContext";
import messagingService from "../../services/messaging/MessagingService";
import {eventType} from "../../config/MessageConstants";
import {UNAUTHORIZED_MESSAGE} from "../../config/Constants";
import AuthSection from "../general/AuthSection";

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

class MyRecipesView extends React.Component {
    isMounted = false;

    constructor(props) {
        super(props);

        this.state = {
            actualPage: 0,
            loadingState: LoadingState.none,
            recipes: [],
            searchCriteria: null,
            totalElement: null,
            totalPages: null,
            errorMessage: null
        };

        messagingService.subscribe(eventType.userLoggedIn, () => {
            if (this.isMounted) {
                //avoid component update in case it is not mounted
                this.fetchUserRecipes();
            }
        });

        messagingService.subscribe(eventType.userLoggedOut, () => {
            if (this.isMounted) {
                //avoid component update in case it is not mounted
                this.forceUpdate();
            }
        });
    }

    componentDidMount() {
        this.isMounted = true;

        this.fetchUserRecipes();
    }

    componentWillUnmount() {
        this.isMounted = false;
    }

    fetchUserRecipes = (pageNumber) => {
        this.setState({
            loadingState: LoadingState.inProgress,
        });

        recipeService.fetchLoggedInUserPageableRecipes(pageNumber)
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

    fetchUserRecipesByCriteria = (searchCriteria, pageNumber) => {
        this.setState({
            loadingState: LoadingState.inProgress,
        });

        recipeService.searchRecipes(RecipeSearchContext.USER_OWN_RECIPE, searchCriteria, pageNumber)
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

        this.fetchUserRecipesByCriteria(searchCriteria);
    }

    changePage = (pageNumber) => {
        this.setState({actualPage: pageNumber});

        const {searchCriteria} = this.state;

        if (searchCriteria) {
            this.fetchUserRecipesByCriteria(searchCriteria, pageNumber);
        } else {
            this.fetchUserRecipes(pageNumber);
        }
    }

    render() {
        const {classes} = this.props;
        const {searchCriteria, loadingState, recipes, totalPages, actualPage, errorMessage} = this.state;

        let recipeCards = [];

        if (ArrayUtils.isNotEmpty(recipes)) {
            //in case we are on the last page and there is only one element on it after deletion we need to fetch the actualPage - 1 page
            //actual page indexed from zero while totalPages is the number of the pages
            if (recipes.length == 1 && actualPage == totalPages - 1) {
                recipeCards.push(
                    <DishbraryRecipeCard key={recipes[0].id} recipeData={recipes[0]}
                                         onDeleteSuccess={() => this.changePage(actualPage - 1)}/>
                );
            } else {
                recipeCards = recipes.map(recipe => {
                    return (<DishbraryRecipeCard key={recipe.id} recipeData={recipe}
                                                 onDeleteSuccess={() => this.fetchUserRecipes(actualPage)}/>)
                })
            }
        }

        return (
            <Paper className={classes.root}>
                <Typography component="h1" variant="h5">
                    Saját receptek
                </Typography>

                <AuthSection unauthorizedMessage={UNAUTHORIZED_MESSAGE}>
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
                                            <DishbraryRecipeSearch searchCriteria={searchCriteria}
                                                                   onSearchTrigger={this.triggerSearch}/>

                                            <div id="recipe-card-container" className={classes.recipeCardContainer}>
                                                <Pagination totalPages={totalPages} actualPage={actualPage}
                                                            onPageChange={this.changePage}>
                                                    {recipeCards}
                                                </Pagination>
                                            </div>
                                        </React.Fragment>
                                    )
                    }
                </AuthSection>
            </Paper>
        )
    }
}

export default withStyles(styles)(MyRecipesView);