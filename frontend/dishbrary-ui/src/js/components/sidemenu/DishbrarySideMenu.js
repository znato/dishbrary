import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';
import SwipeableDrawer from '@material-ui/core/SwipeableDrawer';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText'

import {Link} from "react-router-dom";
import {homePath, calorieTablePath, createRecipePath, userOwnRecipesPath} from '../../config/ApplicationRoutes';

import {Home, Create, AccountBox} from '@material-ui/icons';
import CalorieIcon from '../icons/CalorieIcon';

const styles = {
    link: {
        textDecoration: "none",
    }
}

const menuItems = [
    {
        title: 'Home',
        icon: Home,
        linkTo: homePath
    },
    {
        title: 'Kalóriatáblázat',
        icon: CalorieIcon,
        linkTo: calorieTablePath
    },
    {
        title: 'Saját receptek',
        icon: AccountBox,
        linkTo: userOwnRecipesPath,
    },
    {
        title: 'Új recept',
        icon: Create,
        linkTo: createRecipePath,
        color: "#2ecc71"
    }
]

class DishbrarySideMenu extends React.Component {

    constructor(props) {
        super(props);
    }

    onOpen = () => {

    }

    onClose = () => {
        //call additional callback if present
        if (typeof this.props.onMenuClose === "function") {
            this.props.onMenuClose();
        }
    }

    render() {
        const {open, classes} = this.props;

        return (
            <SwipeableDrawer onClose={this.onClose} onOpen={this.onOpen} open={open}>
                <List>
                    {
                        menuItems.map((menuItem, index) => (
                            <ListItem button key={menuItem.title} id={index} onClick={this.onClose}>
                                <ListItemIcon>
                                    {
                                        typeof menuItem.color === 'undefined'
                                            ?
                                            <menuItem.icon/>
                                            :
                                            <menuItem.icon nativeColor={menuItem.color}/>
                                    }
                                </ListItemIcon>
                                <Link to={menuItem.linkTo} className={classes.link}>
                                    <ListItemText primary={menuItem.title}/>
                                </Link>
                            </ListItem>
                        ))}
                </List>
            </SwipeableDrawer>
        );
    }
}

export default withStyles(styles)(DishbrarySideMenu);