import React from 'react';

import PropTypes from 'prop-types';

import {withStyles} from '@material-ui/core/styles/index';
import Typography from '@material-ui/core/Typography/index';
import AppBar from '@material-ui/core/AppBar/index';
import Toolbar from '@material-ui/core/Toolbar/index';
import IconButton from '@material-ui/core/IconButton/index';
import MenuIcon from '@material-ui/icons/Menu';

import UserManagementBlock from "./UserManagementBlock";
import DishbrarySideMenu from '../sidemenu/DishbrarySideMenu';

import {Link} from "react-router-dom";
import * as ApplicationRoutes from '../../config/ApplicationRoutes';

const styles = {
    root: {
        flexGrow: 1,
    },
    appBar: {
        backgroundColor: "#28282a",
        boxShadow: "none",
    },
    title: {
        // flexGrow: 1,
        fontWeight: 700,
        textTransform: "uppercase",
        '& a': {
            color: "white",
            textDecoration: "none",
        }
    },
    placeHolder: {
        flexGrow: 1,
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

        this.state = {
            sideMenuOpen: false,
        }
    }

    openSideMenu = () => {
        this.setState({sideMenuOpen: true});
    }

    closeSideMenu = () => {
        this.setState({sideMenuOpen: false});
    }

    render() {
        const {classes} = this.props;
        const {sideMenuOpen} = this.state;

        return (
            <div className={classes.root}>
                <AppBar position="fixed" className={classes.appBar}>
                    <Toolbar>
                        <IconButton className={classes.menuButton} color="inherit" aria-label="Menu" onClick={this.openSideMenu}>
                            <MenuIcon />
                        </IconButton>
                        <Typography variant="h6" color="inherit" align="center" className={classes.title}>
                            <Link to={ApplicationRoutes.homePath}>
                                DISHBRARY
                            </Link>
                        </Typography>
                        <span className={classes.placeHolder}>&nbsp;</span>
                        <UserManagementBlock/>
                    </Toolbar>
                </AppBar>

                <DishbrarySideMenu open={sideMenuOpen} onMenuClose={this.closeSideMenu}/>
            </div>
        );
    }
}

DishbraryAppBar.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(DishbraryAppBar);