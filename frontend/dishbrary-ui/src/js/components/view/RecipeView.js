import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';

import {Paper} from "@material-ui/core";
import Typography from "@material-ui/core/Typography";
import CircularProgress from '@material-ui/core/CircularProgress';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText'
import {ArrowRight} from '@material-ui/icons';
import Avatar from '@material-ui/core/Avatar';

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
    recipeContainer: {}
});

class RecipeView extends React.Component {

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

    renderTextIfDefined = (messageBeforeText, text, messageAfterText) => {
        return (
            text
                ?
                <Typography component="p" variant="body2">
                    {messageBeforeText} {text} {messageAfterText}
                </Typography>
                : ""
        );
    }

    renderCategoriesIfDefined = (categories) => {
        if (ArrayUtils.isEmpty(categories)) {
            return "";
        }

        return (
            <div>
                <Typography component="p" variant="body2">
                    Kategóriák:
                </Typography>

                <List>
                    {
                        categories.map((category, index) => (
                            <ListItem key={category.id} id={index}>
                                <ListItemIcon>
                                    <ArrowRight/>
                                </ListItemIcon>
                                <ListItemText primary={category.name}/>
                            </ListItem>
                        ))}
                </List>
            </div>
        );
    }

    renderCuisinesIfDefined = (cuisines) => {
        if (ArrayUtils.isEmpty(cuisines)) {
            return "";
        }

        return (
            <div>
                <Typography component="p" variant="body2">
                    Konyha nemzetisége:
                </Typography>

                <List>
                    {
                        cuisines.map((cuisine, index) => (
                            <ListItem key={cuisine.id} id={index}>
                                <ListItemIcon>
                                    <Avatar src={cuisine.iconUrl}/>
                                </ListItemIcon>
                                <ListItemText primary={cuisine.name}/>
                            </ListItem>
                        ))}
                </List>
            </div>
        );
    }

    renderCalorieInfo = (calorieInfo) => {
        const {energyKcal, protein, fat, carbohydrate} = calorieInfo;

        return (
            <div id="calorie-info-container">
                <Typography variant="body1" color="textSecondary" component="p">
                    Tápértékek egy adagban: Kalória: {energyKcal} kcal, Fehérje: {protein} g, Zsír: {fat} g,
                    Szénhidrát: {carbohydrate} g
                </Typography>
            </div>
        );
    }

    renderIngredients = (ingredientDataList) => {
        return (
            <div id="ingredient-enumeration-container">
                <Typography component="p" variant="body2">
                    Hozzávalók:
                </Typography>

                <List>
                    {
                        ingredientDataList.map((ingredientData, index) => (
                            <ListItem key={ingredientData.id} id={index}>
                                <ListItemIcon>
                                    <ArrowRight/>
                                </ListItemIcon>
                                <ListItemText
                                    primary={ingredientData.quantity + " " + ingredientData.selectedUnit + " " + ingredientData.ingredient.name}/>
                            </ListItem>
                        ))}
                </List>
            </div>
        );
    }

    renderInstruction = (instruction) => {
        return (
            <div id="instruction-container">
                <Typography component="p" variant="body1">
                    Leírás:
                </Typography>

                <div id="description">
                    <p dangerouslySetInnerHTML={{__html: instruction}}/>
                </div>
            </div>
        );
    }

    render() {
        const {classes} = this.props;
        const {loadingState, recipe, errorMessage} = this.state;

        const {
            preparationTimeInMinute,
            cookTimeInMinute,
            ingredients,
            categories,
            cuisines,
            portion,
            calorieInfo,
            instruction,
            creationDate
        } = recipe;

        return (
            <Paper className={classes.root}>
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
                                <Typography>{errorMessage}</Typography>
                                :
                                (
                                    <div id="recipe-container" className={classes.recipeContainer}>
                                        <Typography component="h1" variant="h5">
                                            {recipe.name}
                                        </Typography>

                                        <Typography component="p" variant="body1">
                                            Készítette: {recipe.owner.username}
                                        </Typography>

                                        <Typography component="p" variant="body1">
                                            Készítés ideje: {creationDate}
                                        </Typography>

                                        {this.renderCalorieInfo(calorieInfo)}

                                        {this.renderTextIfDefined("Előkészítési idő:", preparationTimeInMinute, "perc")}

                                        {this.renderTextIfDefined("Elkészítési idő:", cookTimeInMinute, "perc")}

                                        {this.renderTextIfDefined("Adagok száma:", portion, "")}

                                        {this.renderCategoriesIfDefined(categories)}

                                        {this.renderCuisinesIfDefined(cuisines)}

                                        {this.renderIngredients(ingredients)}

                                        {this.renderInstruction(instruction)}
                                    </div>
                                )
                }
            </Paper>
        )
    }
}

export default withStyles(styles)(RecipeView);