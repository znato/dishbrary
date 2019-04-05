import React from 'react';

import PropTypes from 'prop-types';

import {withStyles} from '@material-ui/core/styles/index';
import Typography from '@material-ui/core/Typography/index';
import AppBar from '@material-ui/core/AppBar/index';
import Toolbar from '@material-ui/core/Toolbar/index';
import IconButton from '@material-ui/core/IconButton/index';
import MenuIcon from '@material-ui/icons/Menu';

import UserManagementBlock from "./UserManagementBlock";


const styles = {
    root: {
        flexGrow: 1,
    },
    appBar: {
        backgroundColor: "#28282a",
        boxShadow: "none",
    },
    title: {
        flexGrow: 1,
        fontWeight: 700,
        textTransform: "uppercase",
    },
    accountCircle: {
        fontSize: 24,
    },
    menuButton: {
        marginLeft: -12,
        marginRight: 20,
    },
};

class DishbraryAppBar extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        const {classes} = this.props;

        return (
            <div className={classes.root}>
                <AppBar position="fixed" className={classes.appBar}>
                    <Toolbar>
                        <IconButton className={classes.menuButton} color="inherit" aria-label="Menu">
                            <MenuIcon/>
                        </IconButton>
                        <Typography variant="h6" color="inherit" align="center" className={classes.title}>
                            DISHBRARY
                        </Typography>
                        <UserManagementBlock/>
                    </Toolbar>
                </AppBar>
            </div>
        );
    }
}

DishbraryAppBar.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(DishbraryAppBar);