import React from 'react';

import PropTypes from 'prop-types';

import withStyles from '@material-ui/core/styles/withStyles';
import Button from '@material-ui/core/Button/index';
import Typography from '@material-ui/core/Typography/index';
import Dialog from '@material-ui/core/Dialog/index';
import DialogContent from '@material-ui/core/DialogContent/index';
import Slide from '@material-ui/core/Slide/index';

import FormControl from '@material-ui/core/FormControl/index';
import Input from '@material-ui/core/Input/index';
import InputLabel from '@material-ui/core/InputLabel/index';
import Avatar from '@material-ui/core/Avatar/index';
import CssBaseline from '@material-ui/core/CssBaseline/index';
import AccountCircle from '@material-ui/icons/AccountCircle';
import Paper from '@material-ui/core/Paper/index';

import userService from "../../services/UserService";
import messagingService from "../../services/messaging/MessagingService";
import {eventType} from "../../config/MessageConstants";

const styles = theme => ({
    dialogContent: {
        width: 'auto',
        display: 'block', // Fix IE 11 issue.
        marginLeft: theme.spacing.unit * 3,
        marginRight: theme.spacing.unit * 3,
        [theme.breakpoints.up(400 + theme.spacing.unit * 3 * 2)]: {
            width: 400,
            marginLeft: 'auto',
            marginRight: 'auto',
        },
    },
    paper: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        padding: `${theme.spacing.unit * 2}px ${theme.spacing.unit * 3}px ${theme.spacing.unit * 3}px`,
    },
    avatar: {
        margin: theme.spacing.unit,
        backgroundColor: theme.palette.primary.main,
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        padding: 0,
    },
    submit: {
        marginTop: theme.spacing.unit * 3,
    },
    errorMessage: {
        display: "none",
        textAlign: "center",
        color: "#ba1e1e",
        paddingTop: 5,
        fontFamily: ["Roboto", "Helvetica", "Arial", "sans-serif"]
    }
});


function Transition(props) {
    return <Slide direction="up" {...props} />;
}

class RegisterUserDialog extends React.Component {

    constructor(props) {
        super(props);
    }

    handleClose = () => {
        const surnameInput = document.getElementById("reg-surname");
        const firstnameInput = document.getElementById("reg-firstname");
        const usernameInput = document.getElementById("reg-username");
        const emailInput = document.getElementById("reg-email");
        const passwordInput = document.getElementById("reg-password");
        const passwordConfirmInput = document.getElementById("reg-password-confirm");
        const errorDiv = document.getElementById("reg-errorMessage");

        //on close clear the whole form
        surnameInput.value = "";
        firstnameInput.value= "";
        usernameInput.value = "";
        emailInput.value = "";
        passwordInput.value = "";
        passwordConfirmInput.value = "";
        errorDiv.style.display = "none";

        //call additional callback if present
        if (typeof this.props.onDialogClose === "function") {
            this.props.onDialogClose();
        }

    };

    handleRegistrationSuccess = (user) => {
        this.handleClose();

        messagingService.publish(eventType.registrationSuccess, user);
    };

    handleRegistrationFailure = (errorMessage) => {
        const errorDiv = document.getElementById("reg-errorMessage");

        errorDiv.firstChild.innerHTML = errorMessage;

        errorDiv.style.display = "block";
    }

    registerUser = (event) => {
        event.preventDefault();

        const passwordConfirmInput = document.getElementById("reg-password-confirm");

        const userData = this.createUserByFormInput();

        if (userData.password !== passwordConfirmInput.value) {
            this.handleRegistrationFailure("A Jelszó megerősítés nem egyezik a Jelszó mező értékével!");
            return;
        }

        userService.register(userData)
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.handleRegistrationFailure(jsonResponse.message);
                } else {
                    this.handleRegistrationSuccess(jsonResponse.content);
                }
            })
    }

    createUserByFormInput = () => {
        const surnameInput = document.getElementById("reg-surname");
        const firstnameInput = document.getElementById("reg-firstname");
        const emailInput = document.getElementById("reg-email");
        const usernameInput = document.getElementById("reg-username");
        const passwordInput = document.getElementById("reg-password");

        return {
            username: usernameInput.value,
            firstName: firstnameInput.value,
            lastName: surnameInput.value,
            email: emailInput.value,
            password: passwordInput.value
        }
    }

    render() {
        const {classes} = this.props;

        return (
            <Dialog
                open={this.props.open}
                TransitionComponent={Transition}
                keepMounted
                onClose={this.handleClose}
                aria-labelledby="alert-dialog-slide-title"
                aria-describedby="alert-dialog-slide-description"
            >
                <DialogContent className={classes.dialogContent}>
                    <CssBaseline/>
                    <Paper className={classes.paper}>
                        <Avatar className={classes.avatar}>
                            <AccountCircle/>
                        </Avatar>
                        <Typography component="h1" variant="h5">
                            REGISZTRÁCIÓ
                        </Typography>
                        <form className={classes.form} onSubmit={this.registerUser} autoComplete="off">
                            <FormControl margin="normal" required fullWidth>
                                <InputLabel htmlFor="reg-username">Felhasználónév</InputLabel>
                                <Input id="reg-username" name="reg-username"
                                       autoComplete="off"/>
                            </FormControl>
                            <FormControl margin="normal" required fullWidth>
                                <InputLabel htmlFor="reg-password">Jelszó</InputLabel>
                                <Input id="reg-password" name="reg-password" type="password"
                                       autoComplete="off"/>
                            </FormControl>
                            <FormControl margin="normal" required fullWidth>
                                <InputLabel htmlFor="reg-password-confirm">Jelszó megerősítése</InputLabel>
                                <Input id="reg-password-confirm" name="reg-password-confirm" type="password"
                                       autoComplete="off"/>
                            </FormControl>
                            <FormControl margin="normal" fullWidth>
                                <InputLabel htmlFor="reg-surname">Vezetéknév</InputLabel>
                                <Input id="reg-surname" name="reg-surname" autoFocus autoComplete="off"/>
                            </FormControl>
                            <FormControl margin="normal" fullWidth>
                                <InputLabel htmlFor="reg-firstname">Keresztnév</InputLabel>
                                <Input id="reg-firstname" name="reg-firstname" autoComplete="off"/>
                            </FormControl>
                            <FormControl margin="normal" fullWidth>
                                <InputLabel htmlFor="reg-email">Email cím</InputLabel>
                                <Input id="reg-email" name="reg-email" type="email" autoComplete="off"/>
                            </FormControl>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                color="primary"
                                className={classes.submit}
                            >
                                Regisztrálok
                            </Button>

                            <div id="reg-errorMessage" className={classes.errorMessage}>
                                <p></p>
                            </div>
                        </form>
                    </Paper>
                </DialogContent>
            </Dialog>
        );
    }
}

RegisterUserDialog.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(RegisterUserDialog);
