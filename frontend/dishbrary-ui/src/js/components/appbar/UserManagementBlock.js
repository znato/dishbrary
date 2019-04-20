import React from "react";

import PropTypes from "prop-types";

import {withStyles} from "@material-ui/core/styles/index";
import Button from "@material-ui/core/Button/index";

import LoginFormDialog from "./LoginFormDialog";
import RegisterUserDialog from "./RegisterUserDialog";

import userService from "../../services/UserService";
import messagingService from "../../services/messaging/MessagingService";

import {eventType} from "../../config/MessageConstants";

const styles = {
    button : {
        fontWeight: "bold",
    }
};

class UserManagementBlock extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            loggedIn: false,
            loginDialogOpened: false,
            registerDialogOpened: false
        }

        messagingService.subscribe(eventType.loginSuccess, this.handleLoginSuccess);

        this.loginFormRef = React.createRef();
    }

    openLoginDialog = () => {
        this.setState({loginDialogOpened: true})
    }

    openRegisterDialog = () => {
        this.setState({registerDialogOpened: true})
    }

    closeLoginDialog = () => {
        this.setState({loginDialogOpened: false})
    }

    closeRegisterDialog = () => {
        this.setState({registerDialogOpened: false})
    }

    handleLoginSuccess = (user) => {
        this.setState({loggedIn: true});
    }

    logout = () => {
        userService.logout()
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    //todo: handle logout error
                } else {
                    messagingService.publish("logoutSuccess");
                    this.setState({loggedIn: false});
                }
        })
    }

    render() {
        const {loggedIn, loginDialogOpened, registerDialogOpened} = this.state;
        const {classes} = this.props;

        return (
            <div className={classes.root}>
                {loggedIn
                    ?
                    (
                        <Button className={classes.button} color="secondary" onClick={this.logout}>Kijelentkezes</Button>
                    )
                    :
                    (
                        <React.Fragment>
                            <Button className={classes.button} color="primary" onClick={this.openLoginDialog}>Bejelentkezés</Button>
                            <LoginFormDialog open={loginDialogOpened} onDialogClose={this.closeLoginDialog}/>
                            <Button className={classes.button} color="secondary" onClick={this.openRegisterDialog}>Regisztráció</Button>
                            <RegisterUserDialog open={registerDialogOpened} onDialogClose={this.closeRegisterDialog}/>
                        </React.Fragment>
                    )
                }
            </div>
        );
    }
}


UserManagementBlock.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(UserManagementBlock);