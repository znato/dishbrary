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
import ShareIcon from '@material-ui/icons/Share';

import recipeService from "../../services/RecipeService";

const styles = theme => ({
    card: {
        maxWidth: 345,
        display: "inline-block",
        margin: "0 5px 0 5px",
        textAlign: "center"
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
    }

    render() {
        const {classes, recipeData} = this.props;

        const {energyKcal, protein, fat, carbohydrate} = recipeData.calorieInfo;

        let recipeName = recipeData.name;

        if (recipeName.length > 20) {
            recipeName = recipeName.substring(0, 20) + "...";
        }

        return (
            <Card className={classes.card}>
                <CardHeader
                    avatar={
                        <Avatar aria-label="recipe" className={classes.avatar}>
                            {recipeData.owner.username.charAt(0)}
                        </Avatar>
                    }
                    title={recipeName}
                    subheader={recipeData.creationDate}
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
                <CardActions disableActionSpacing>
                    <IconButton aria-label="add to favorites">
                        <FavoriteIcon />
                    </IconButton>
                    <IconButton aria-label="share">
                        <ShareIcon />
                    </IconButton>
                </CardActions>
            </Card>
        );
    }
}

export default withStyles(styles)(DishbraryRecipeCard);