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
import Collapse from '@material-ui/core/Collapse';

import ReactPlayer from 'react-player';

import {Carousel} from 'react-responsive-carousel';
import 'react-responsive-carousel/lib/styles/carousel.min.css';
import '../../../css/carouselRestyle.pure.css';

import backgroundImg from '../../../images/recipe-view-background.jpg';

import recipeService from "../../services/RecipeService";
import {LoadingState} from "../../services/constants/LoadingState";

import * as ArrayUtils from '../../services/utils/ArrayUtils';

const styles = theme => ({
    root: {
        width: '100%',
        marginTop: theme.spacing.unit * 3,
        overflowX: 'auto',
        textAlign: "center",
        background: 'url(\'' + backgroundImg + '\') no-repeat center center fixed',
        backgroundSize: 'cover'
    },
    progress: {
        margin: theme.spacing.unit * 2,
    },
    videoContainer: {
        margin: "25px auto",
    },
    toggleButton: {
        border: 0,
        background: "transparent",
        cursor: "pointer"
    },
    videoPlayer: {
        margin: "auto",
    },
    carouselContainer: {
        width: '30em',
        margin: 'auto'
    },
    recipeContainer: {
        width: "50em",
        margin: "auto",
        backgroundColor: "#c9c0b9"
    },
    recipeDataContainer: {
        width: '30em',
        margin: 'auto',
        marginTop: "25px",
        textAlign: 'left'
    }
});

class RecipeView extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            loadingState: LoadingState.none,
            showVideo: false,
            showCategories: true,
            showCuisines: true,
            showIngredients: true,
            selectedCarouselItemNumber: 0,
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

    toggle = (propName) => () => {
        const prop = this.state[propName];
        this.setState({[propName]: !prop})
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

    renderVideoIfDefined = (recipe) => {
        if (!recipe.videoFileName) {
            return "";
        }

        const {classes} = this.props;
        const {showVideo} = this.state;

        return (
            <div className={classes.videoContainer}>
                <button id="hideOrShowVideo" onClick={this.toggle('showVideo')} className={classes.toggleButton}>
                    <Typography component="p" variant="body2">
                        {showVideo ? "Rejtsd el" : "Mutasd"} a videót
                    </Typography>
                </button>

                <Collapse in={showVideo}>
                    <ReactPlayer className={classes.videoPlayer}
                                 url={recipeService.getRecipeVideoPath(recipe.id, recipe.videoFileName)}
                                 controls={true}/>
                </Collapse>
            </div>
        );
    }

    renderImagesIfDefined = (recipe) => {
        if (ArrayUtils.isEmpty(recipe.additionalImagesFileNames)) {
            return "";
        }

        const {classes} = this.props;
        const {selectedCarouselItemNumber} = this.state;

        return (
            <div className={classes.carouselContainer}>
                <Carousel showThumbs={false}
                          autoPlay
                          infiniteLoop
                          selectedItem={selectedCarouselItemNumber}
                          onChange={this.carouselChanged}>
                    {
                        recipe.additionalImagesFileNames.map((fileName, index) => (
                            <div key={index}>
                                <img src={recipeService.getRecipeImagePath(recipe.id, fileName)}/>
                            </div>
                        ))
                    }
                </Carousel>
            </div>
        );
    }

    carouselChanged = (selectedCarouselItemNumber) => {
        this.state.selectedCarouselItemNumber = selectedCarouselItemNumber;
    }

    renderCategoriesIfDefined = (categories) => {
        if (ArrayUtils.isEmpty(categories)) {
            return "";
        }

        const {classes} = this.props;
        const {showCategories} = this.state;

        return (
            <div>
                <button id="hideOrShowCategories" onClick={this.toggle('showCategories')}
                        className={classes.toggleButton}>
                    <Typography component="p" variant="body2">
                        {showCategories ? "Rejtsd el" : "Mutasd"} a kategóriákat
                    </Typography>
                </button>

                <Collapse in={showCategories}>
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
                </Collapse>
            </div>
        );
    }

    renderCuisinesIfDefined = (cuisines) => {
        if (ArrayUtils.isEmpty(cuisines)) {
            return "";
        }

        const {classes} = this.props;
        const {showCuisines} = this.state;

        return (
            <div>
                <button id="hideOrShowCuisines" onClick={this.toggle('showCuisines')}
                        className={classes.toggleButton}>
                    <Typography component="p" variant="body2">
                        {showCuisines ? "Rejtsd el" : "Mutasd"} konyha nemzetiségeket:
                    </Typography>
                </button>

                <Collapse in={showCuisines}>
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
                </Collapse>
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
        const {classes} = this.props;
        const {showIngredients} = this.state;

        return (
            <div id="ingredient-enumeration-container">
                <button id="hideOrShowIngredients" onClick={this.toggle('showIngredients')}
                        className={classes.toggleButton}>
                    <Typography component="p" variant="body2">
                        {showIngredients ? "Rejtsd el" : "Mutasd"} hozzávalókat:
                    </Typography>
                </button>

                <Collapse in={showIngredients}>
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
                </Collapse>
            </div>
        );
    }

    renderInstruction = (instruction) => {
        return (
            <div id="instruction-container">
                <Typography component="p" variant="body2">
                    Elkészítés:
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

        const cDate = new Date(creationDate);
        const formattedCreationDate = cDate.getFullYear() + "/" + (cDate.getMonth() + 1) + "/" + cDate.getDate();

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

                                        {this.renderVideoIfDefined(recipe)}

                                        {this.renderImagesIfDefined(recipe)}

                                        <div className={classes.recipeDataContainer}>
                                            <Typography component="p" variant="body2">
                                                Készítette: {recipe.owner.username}
                                            </Typography>

                                            <Typography component="p" variant="body2">
                                                Készítés ideje: {formattedCreationDate}
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
                                    </div>
                                )
                }
            </Paper>
        )
    }
}

export default withStyles(styles)(RecipeView);