import React from 'react';
import withStyles from '@material-ui/core/styles/withStyles';

import Typography from '@material-ui/core/Typography';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import {red} from '@material-ui/core/colors';
import FavoriteIcon from '@material-ui/icons/Favorite';
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';
import Tooltip from '@material-ui/core/Tooltip';

import {Link} from "react-router-dom";

import {isNotBlank} from '../../services/utils/StringUtils';
import * as ApplicationRoutes from '../../config/ApplicationRoutes';

import recipeService from "../../services/RecipeService";
import DishbraryAlertDialog from "../general/DishbraryAlertDialog";
import DishbraryConfirmDialog from "../general/DishbraryConfirmDialog";
import ApplicationState from "../../ApplicationState";

const styles = theme => ({
    card: {
        minWidth: 290,
        minHeight: 460,
        maxWidth: 345,
        display: "inline-block",
        margin: "0 5px 0 5px",
        textAlign: "center",
        '& a': {
            color: "white",
            textDecoration: "none",
        }
    },
    cardActionForOwnRecipes: {
        float: "right"
    },
    media: {
        height: 0,
        paddingTop: '56.25%', // 16:9
        backgroundSize: 'contain'
    },
    expand: {
        transform: 'rotate(0deg)',
        marginLeft: 'auto',
        transition: theme.transitions.create('transform', {
            duration: theme.transitions.duration.shortest,
        }),
    },
    expandOpen: {
        transform: 'rotate(180deg)',
    },
    avatar: {
        backgroundColor: red[500],
    },
});

class DishbraryRecipeCard extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            recipeAddedToFavourites: false,
            recipeRemovedFromFavourites: false,
            alertData: {
                openAlert: false,
                alertDialogTitle: "",
                alertDialogContent: ""
            },
            confirmData: {
                openConfirm: false,
                confirmDialogTitle: "",
                confirmDialogContent: ""
            }
        }
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

    openConfirmDialog = (title, message) => () => {
        this.setState(
            {
                confirmData: {
                    openConfirm: true,
                    confirmDialogTitle: title,
                    confirmDialogContent: message
                }
            })
    }

    closeConfirmDialog = () => {
        this.setState(
            {
                confirmData: {
                    openConfirm: false,
                    confirmDialogTitle: "",
                    confirmDialogContent: ""
                }
            })
    }

    deleteRecipe = (recipeId) => () => {
        recipeService.deleteRecipe(recipeId)
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.openAlertDialog("Hiba történt!", jsonResponse.message);
                } else {
                    //call additional callback if present
                    if (typeof this.props.onDeleteSuccess === "function") {
                        this.props.onDeleteSuccess(recipeId);
                    }
                }
            });
    }

    addRecipeToFavourites = (recipeId) => () => {
        recipeService.addRecipeToFavourites(recipeId)
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.openAlertDialog("Hiba történt!", jsonResponse.message);
                } else {
                    this.setState({
                        recipeAddedToFavourites: true,
                        recipeRemovedFromFavourites: false
                    });
                }
            });
    }

    deleteRecipeFromFavourites = (recipeId) => () => {
        recipeService.deleteRecipeFromFavourites(recipeId)
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.openAlertDialog("Hiba történt!", jsonResponse.message);
                } else {
                    this.setState({
                        recipeAddedToFavourites: false,
                        recipeRemovedFromFavourites: true
                    });
                }
            });
    }

    render() {
        const {classes, recipeData} = this.props;
        const {confirmData, alertData, recipeAddedToFavourites, recipeRemovedFromFavourites} = this.state;

        const isRecipeFavourite = (recipeData.favourite && !recipeRemovedFromFavourites) || recipeAddedToFavourites;

        const {energyKcal, protein, fat, carbohydrate} = recipeData.calorieInfo;

        let recipeName = recipeData.name;

        if (recipeName.length > 20) {
            recipeName = recipeName.substring(0, 20) + "...";
        }

        const ownerAvatar = isNotBlank(recipeData.owner.profileImageUrl)
            ? (
                <Avatar aria-label="recipe" src={recipeData.owner.profileImageUrl} />
            )
            : (
                <Avatar aria-label="recipe" className={classes.avatar}>
                    {recipeData.owner.username.charAt(0)}
                </Avatar>
            );

        const cDate = new Date(recipeData.creationDate);
        const formattedCreationDate = cDate.getFullYear() + "/" + (cDate.getMonth() + 1) + "/" + cDate.getDate();

        return (
            <Card className={classes.card}>
                <Link to={ApplicationRoutes.viewRecipePath + "/" + recipeData.id}>
                    <CardHeader
                        avatar={ownerAvatar}
                        title={recipeName}
                        subheader={formattedCreationDate}
                    />
                    <CardMedia
                        className={classes.media}
                        image={recipeService.getRecipeImagePath(recipeData.id, recipeData.coverImageFileName)}
                    />
                    <CardContent>
                        <Typography variant="body1" color="textSecondary" component="p">
                            Tápértékek egy adagban
                        </Typography>

                        <Typography variant="body2" color="textSecondary" component="p">
                            Kalória: {energyKcal} kcal
                        </Typography>
                        <Typography variant="body2" color="textSecondary" component="p">
                            Fehérje: {protein} g
                        </Typography>
                        <Typography variant="body2" color="textSecondary" component="p">
                            Zsír: {fat} g
                        </Typography>
                        <Typography variant="body2" color="textSecondary" component="p">
                            Szénhidrát: {carbohydrate} g
                        </Typography>
                    </CardContent>
                </Link>
                {
                    ApplicationState.isUserAuthenticated && recipeData.editable ?
                        <CardActions className={classes.cardActionForOwnRecipes} disableActionSpacing>
                            <React.Fragment>
                                <Link to={ApplicationRoutes.editRecipePath + "/" + recipeData.id}>
                                    <Tooltip title="Recept szerkesztése" aria-label="edit-recipe">
                                        <IconButton aria-label="edit recipe">
                                            <EditIcon/>
                                        </IconButton>
                                    </Tooltip>
                                </Link>
                                <Tooltip title="Recept törlése" aria-label="del-recipe">
                                    <IconButton aria-label="delete recipe"
                                                onClick={this.openConfirmDialog("Megerősítés", "Biztosan törlöd a receptet?")}>
                                        <DeleteIcon/>
                                    </IconButton>
                                </Tooltip>
                            </React.Fragment>
                        </CardActions>
                        : ""
                }
                {
                    ApplicationState.isUserAuthenticated && ((recipeData.likeable && !isRecipeFavourite) || recipeRemovedFromFavourites) ?
                        <CardActions disableActionSpacing>
                            <Tooltip title="Kedvencekhez adás" aria-label="add-to-fav">
                                <IconButton aria-label="add to favorites" onClick={this.addRecipeToFavourites(recipeData.id)}>
                                    <FavoriteIcon/>
                                </IconButton>
                            </Tooltip>
                        </CardActions>
                        : ""
                }
                {
                    ApplicationState.isUserAuthenticated && isRecipeFavourite ?
                        <CardActions disableActionSpacing>
                            <Tooltip title="Eltávolítás a kedvencekhez közül" aria-label="remove-from-fav">
                                <IconButton aria-label="remove from favorites" onClick={this.deleteRecipeFromFavourites(recipeData.id)}>
                                    <FavoriteIcon color="secondary"/>
                                </IconButton>
                            </Tooltip>
                        </CardActions>
                        : ""
                }

                <DishbraryAlertDialog open={alertData.openAlert}
                                      dialogTitle={alertData.alertDialogTitle}
                                      dialogContent={alertData.alertDialogContent}
                                      onAlertDialogClose={this.closeAlertDialog}/>

                <DishbraryConfirmDialog open={confirmData.openConfirm}
                                        dialogTitle={confirmData.confirmDialogTitle}
                                        dialogContent={confirmData.confirmDialogContent}
                                        onActionNo={this.closeConfirmDialog}
                                        onActionYes={this.deleteRecipe(recipeData.id)}/>
            </Card>
        );
    }
}

export default withStyles(styles)(DishbraryRecipeCard);