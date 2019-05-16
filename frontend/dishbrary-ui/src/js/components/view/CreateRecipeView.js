import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';

import RecipeEditor from '../editor/RecipeEditor';
import Typography from "@material-ui/core/Typography";
import {Paper} from "@material-ui/core";

const styles = {}

class CreateRecipeView extends React.Component {

    constructor(props) {
        super(props);

    }

    render() {
        return (
            <Paper>
                <Typography component="h1" variant="h5">
                    Új recept felvétele
                </Typography>

                <RecipeEditor/>
            </Paper>
        )
    }
}

export default withStyles(styles)(CreateRecipeView);