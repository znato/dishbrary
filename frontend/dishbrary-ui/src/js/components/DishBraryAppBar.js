import React from 'react';

import PropTypes from 'prop-types';

import {withStyles} from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import AccountCircle from '@material-ui/icons/AccountCircle';
import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';

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

class DishBraryAppBar extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            auth: true,
            anchorEl: null,
        };

        this.handleMenu = this.handleMenu.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }

    handleMenu(event) {
        this.setState({anchorEl: event.currentTarget});
    };

    handleClose() {
        this.setState({anchorEl: null});
    };

    render() {
        const {classes} = this.props;
        const {auth, anchorEl} = this.state;
        const open = Boolean(anchorEl);

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
                        {auth && (
                            <div>
                                <IconButton
                                    aria-owns={open ? 'menu-appbar' : undefined}
                                    aria-haspopup="true"
                                    onClick={this.handleMenu}
                                    color="inherit">
                                    <AccountCircle className={classes.accountCircle}/>
                                </IconButton>
                                <Menu
                                    id="menu-appbar"
                                    anchorEl={anchorEl}
                                    anchorOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    transformOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    open={open}
                                    onClose={this.handleClose}
                                >
                                    <MenuItem onClick={this.handleClose}>Belépés</MenuItem>
                                    <MenuItem onClick={this.handleClose}>Regisztráció</MenuItem>
                                </Menu>
                            </div>
                        )}
                    </Toolbar>
                </AppBar>
            </div>
        );
    }
}

DishBraryAppBar.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(DishBraryAppBar);