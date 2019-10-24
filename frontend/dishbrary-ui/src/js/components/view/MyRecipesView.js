import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';

import {Paper} from "@material-ui/core";
import Typography from "@material-ui/core/Typography";

import CircularProgress from '@material-ui/core/CircularProgress';

import DishbraryRecipeCard from "../recipe/DishbraryRecipeCard"
import recipeService from "../../services/RecipeService";
import {LoadingState} from "../../services/constants/LoadingState";

import * as ArrayUtils from '../../services/utils/ArrayUtils';

const styles = theme => ({
    root: {
        width: '100%',
        marginTop: theme.spacing.unit * 3,
        overflowX: 'auto',
        textAlign: "center",
    },
    progress: {
        margin: theme.spacing.unit * 2,
    },
});

class MyRecipesView extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            loadingState: LoadingState.none,
            recipes: [],
            totalElement: null,
            totalPages: null
        }
    }

    componentDidMount() {
        this.fetchUserRecipes();
    }

    fetchUserRecipes = () => {
        this.setState({
            loadingState: LoadingState.inProgress,
        });

        recipeService.fetchLoggedInUserPageableRecipes()
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({loadingState: LoadingState.error});
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

    render() {
        const {classes} = this.props;
        const {loadingState, recipes, totalElement, totalPages} = this.state;

        let recipeCards = [];

        if (ArrayUtils.isNotEmpty(recipes)) {
            recipeCards = recipes.map(recipe => {
                return (<DishbraryRecipeCard key={recipe.id} recipeData={recipe}/>)
            })
        }

        return (
            <Paper className={classes.root}>
                <Typography component="h1" variant="h5">
                    Saját receptek
                </Typography>

                {
                    loadingState === LoadingState.inProgress
                    ?
                    <CircularProgress disableShrink={true} className={classes.progress}/>
                    :
                        loadingState === LoadingState.none
                        ?
                        ""
                        :
                        loadingState === LoadingState.error
                        ?
                            <Typography>Az oldal jelenleg nem elérhető! Kérjük próbálja később!</Typography>
                        :
                        (
                            <div id="recipe-cards">
                                {recipeCards}
                            </div>
                        )
                }
            </Paper>
        )
    }
}

export default withStyles(styles)(MyRecipesView);