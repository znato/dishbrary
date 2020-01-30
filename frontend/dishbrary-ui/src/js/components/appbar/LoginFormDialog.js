import React from 'react';

import PropTypes from 'prop-types';

import withStyles from '@material-ui/core/styles/withStyles';
import Button from '@material-ui/core/Button/index';
import Typography from '@material-ui/core/Typography/index';
import Dialog from '@material-ui/core/Dialog/index';
import DialogContent from '@material-ui/core/DialogContent/index';
import Slide from '@material-ui/core/Slide/index';

import FormControl from '@material-ui/core/FormControl/index';
import FormControlLabel from '@material-ui/core/FormControlLabel/index';
import Checkbox from '@material-ui/core/Checkbox/index';
import Input from '@material-ui/core/Input/index';
import InputLabel from '@material-ui/core/InputLabel/index';
import Avatar from '@material-ui/core/Avatar/index';
import CssBaseline from '@material-ui/core/CssBaseline/index';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
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
        backgroundColor: theme.palette.secondary.main,
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

class LoginFormDialog extends React.Component {

    constructor(props) {
        super(props);
    }

    handleClose = () => {
        const usernameInput = document.getElementById("username");
        const passwordInput = document.getElementById("password");
        const errorDiv = document.getElementById("errorMessage");

        //on close clear the whole form
        usernameInput.value = "";
        passwordInput.value = "";
        errorDiv.style.display = "none";

        //call additional callback if present
        if (typeof this.props.onDialogClose === "function") {
            this.props.onDialogClose();
        }

    };

    handleLoginSuccess = (user) => {
        this.handleClose();

        messagingService.publish(eventType.userLoggedIn, user);
    };

    handleLoginFailure = (errorMessage) => {
        const errorDiv = document.getElementById("errorMessage");

        errorDiv.firstChild.innerHTML = errorMessage;

        errorDiv.style.display = "block";
    }

    attemptLogin = (event) => {
        event.preventDefault();

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        username && password && userService.login(username, password)
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.handleLoginFailure(jsonResponse.message);
                } else {
                    this.handleLoginSuccess(jsonResponse.content);
                }
            })
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
                            <LockOutlinedIcon/>
                        </Avatar>
                        <Typography component="h1" variant="h5">
                            BEJELENTKEZÉS
                        </Typography>
                        <form className={classes.form} onSubmit={this.attemptLogin}>
                            <FormControl margin="normal" required fullWidth>
                                <InputLabel htmlFor="username">Felhasználónév</InputLabel>
                                <Input id="username" name="username"
                                       autoComplete="username" autoFocus/>
                            </FormControl>
                            <FormControl margin="normal" required fullWidth>
                                <InputLabel htmlFor="password">Jelszó</InputLabel>
                                <Input id="password" name="password" type="password"
                                       autoComplete="current-password"/>
                            </FormControl>
                            <FormControlLabel
                                control={<Checkbox value="remember" color="primary"/>}
                                label="Maradj bejelentkezve"
                            />
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                color="primary"
                                className={classes.submit}
                            >
                                Bejelentkezés
                            </Button>

                            <div id="errorMessage" className={classes.errorMessage}>
                                <p></p>
                            </div>
                        </form>
                    </Paper>
                </DialogContent>
            </Dialog>
        );
    }
}

LoginFormDialog.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(LoginFormDialog);
