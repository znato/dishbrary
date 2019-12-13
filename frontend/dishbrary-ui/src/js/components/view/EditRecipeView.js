import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';

import RecipeEditor from '../editor/RecipeEditor';
import Typography from "@material-ui/core/Typography";
import {Paper} from "@material-ui/core";
import {LoadingState} from "../../services/constants/LoadingState";
import recipeService from "../../services/RecipeService";
import DishbraryProgress from "../general/DishbraryProgress";

const styles = theme => ({
    root: {
        minHeight: '100vh',
        width: '100%',
        marginTop: theme.spacing.unit * 3,
        overflowX: 'auto',
        textAlign: "center",
    }
});

class EditRecipeView extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            loadingState: LoadingState.none,
            recipe: {},
            errorMessage: null
        }
    }

    componentDidMount() {
        const {
            params: {recipeId}
        } = this.props.match;

        this.fetchRecipeById(recipeId);
    }

    fetchRecipeById = (recipeid) => {
        this.setState({
            loadingState: LoadingState.inProgress,
        });

        recipeService.fetchRecipeById(recipeid)
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({
                        loadingState: LoadingState.error,
                        errorMessage: jsonResponse.message
                    });
                } else {
                    this.setState({
                        loadingState: LoadingState.loaded,
                        recipe: jsonResponse.content,
                    });
                }
            });
    }

    render() {
        const {classes} = this.props;

        const {loadingState, recipe, errorMessage} = this.state;

        return (
            <Paper className={classes.root}>
                <Typography component="h1" variant="h5">
                    Recept szerkesztése
                </Typography>

                {
                    loadingState === LoadingState.inProgress
                        ?
                        <DishbraryProgress disableShrink={true} className={classes.progress}/>
                        :
                        loadingState === LoadingState.none
                            ?
                            ""
                            :
                            loadingState === LoadingState.error
                                ?
                                <Typography>{errorMessage}</Typography>
                                :
                                (<RecipeEditor recipe={recipe} />)
                }
            </Paper>
        )
    }
}

export default withStyles(styles)(EditRecipeView);