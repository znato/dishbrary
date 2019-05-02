import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';
import SwipeableDrawer from '@material-ui/core/SwipeableDrawer';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText'

import {Link} from "react-router-dom";
import  {calorieTablePath} from '../../config/ApplicationRoutes';

import CalorieIcon from '../icons/CalorieIcon'

const styles = {
    link: {
        textDecoration: "none",
    }
}

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
        const {open,classes} = this.props;

        return (
            <SwipeableDrawer onClose={this.onClose} onOpen={this.onOpen} open={open}>
                <List>
                    {['Kalóriatáblázat'].map((text, index) => (
                        <ListItem button key={text} id={index} onClick={this.onClose}>
                            <ListItemIcon><CalorieIcon/></ListItemIcon>
                            <Link to={calorieTablePath} className={classes.link}>
                                <ListItemText primary={text} />
                            </Link>
                        </ListItem>
                    ))}
                </List>
            </SwipeableDrawer>
        );
    }
}

export default withStyles(styles)(DishbrarySideMenu);