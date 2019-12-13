import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';

import RecipeEditor from '../editor/RecipeEditor';
import Typography from "@material-ui/core/Typography";
import {Paper} from "@material-ui/core";

const styles = theme => ({
    root: {
        minHeight: '100vh',
        width: '100%',
        marginTop: theme.spacing.unit * 3,
        overflowX: 'auto',
        textAlign: "center",
    }
});

class CreateRecipeView extends React.Component {

    constructor(props) {
        super(props);

    }

    render() {
        const {classes} = this.props;

        return (
            <Paper className={classes.root}>
                <Typography component="h1" variant="h5">
                    Új recept felvétele
                </Typography>

                <RecipeEditor/>
            </Paper>
        )
    }
}

export default withStyles(styles)(CreateRecipeView);