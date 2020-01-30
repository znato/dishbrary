import React from "react";
import {LoadingState} from "../../services/constants/LoadingState";
import DishbraryProgress from "../general/DishbraryProgress";
import Typography from "@material-ui/core/es/Typography";
import FormControl from "@material-ui/core/FormControl";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import userService from "../../services/UserService";
import withStyles from "@material-ui/core/styles/withStyles";
import {Paper} from "@material-ui/core";

const styles = theme => ({
    root: {
        minHeight: '100vh',
        width: '100%',
        marginTop: theme.spacing.unit * 3,
        overflowX: 'auto',
        textAlign: "center",
    },
    form: {
        width: "80%",
        margin: "0 10% 0 10%",
    },
});

class EditAccountView extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            loadingState: LoadingState.none,
            currentPassword: null,
            errorMessage: null,
            passwordChangeIntended: false,
            confirmedPassword: null,
            dataEdited: false,
            user: {
                username: "",
                firstName: "",
                lastName: "",
                email: "",
                password: "",
            },
        }
    }

    componentDidMount() {
        this.fetchUserData();
    }

    fetchUserData = () => {
        userService.getUserData()
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({
                        loadingState: LoadingState.error,
                        errorMessage: jsonResponse.message
                    });
                } else {
                    this.setState({
                        loadingState: LoadingState.loaded,
                        user: jsonResponse.content
                    });
                }
            });
    }

    onUserDataChange = (userPropName) => (event) => {
        let user = this.state.user;

        user[userPropName] = event.target.value;

        this.setState({
            user, dataEdited: true
        });
    }

    onPasswordChange = (event) => {
        const pwdText = event.target.value;

        let user = this.state.user;
        user.password = pwdText;

        let newState = {user};

        if (pwdText && pwdText.trim() != "") {
            newState.passwordChangeIntended = true;
        } else {
            newState.passwordChangeIntended = false;
            newState.confirmedPassword = null;
        }

        this.setState(newState);
    }

    onEventBasedInputChange = (propName) => (event) => {
        this.setState({
            [propName]: event.target.value,
        });
    }

    saveUserData = (event) => {
        event.preventDefault();

    }

    render() {
        const {classes} = this.props;
        const {loadingState, user, dataEdited, passwordChangeIntended, confirmedPassword, currentPassword, errorMessage} = this.state;


        const askForCurrentPassword = (!passwordChangeIntended && dataEdited) || (passwordChangeIntended && user.password === confirmedPassword);

        const readyToSave = askForCurrentPassword && currentPassword && currentPassword.trim() != "";

        return (
            <Paper className={classes.root}>
                <Typography component="h1" variant="h5">
                    Fiók szerkesztése
                </Typography>

                {
                    loadingState === LoadingState.inProgress
                        ?
                        <DishbraryProgress/>
                        :
                        loadingState === LoadingState.none
                            ?
                            ""
                            :
                            loadingState === LoadingState.error ?
                                <Typography>{errorMessage}</Typography>
                                :
                                (
                                    <div id="user-data-container">
                                        <form className={classes.form} onSubmit={this.saveUserData} autoComplete="off">
                                            <FormControl margin="normal" required fullWidth>
                                                <TextField
                                                    label="Felhasználónév:"
                                                    value={user.username || ""}
                                                    onChange={this.onUserDataChange('username')}
                                                />
                                            </FormControl>

                                            <FormControl margin="normal" fullWidth>
                                                <TextField
                                                    autoComplete="new-password"
                                                    label="Új Jelszó:"
                                                    type="password"
                                                    value={user.password || ""}
                                                    onChange={this.onPasswordChange}
                                                />
                                            </FormControl>

                                            {passwordChangeIntended
                                                ?
                                                <FormControl margin="normal" fullWidth>
                                                    <TextField
                                                        autoComplete="new-password"
                                                        label="Új Jelszó megerősítése:"
                                                        type="password"
                                                        value={confirmedPassword || ""}
                                                        onChange={this.onEventBasedInputChange('confirmedPassword')}
                                                    />
                                                </FormControl>
                                                : ""
                                            }

                                            <FormControl margin="normal" fullWidth>
                                                <TextField
                                                    label="Vezetéknév:"
                                                    value={user.firstName || ""}
                                                    onChange={this.onUserDataChange('firstName')}
                                                />
                                            </FormControl>

                                            <FormControl margin="normal" fullWidth>
                                                <TextField
                                                    label="Keresztnév:"
                                                    value={user.lastName || ""}
                                                    onChange={this.onUserDataChange('lastName')}
                                                />
                                            </FormControl>

                                            <FormControl margin="normal" fullWidth>
                                                <TextField
                                                    label="Email cím:"
                                                    value={user.email || ""}
                                                    onChange={this.onUserDataChange('email')}
                                                />
                                            </FormControl>

                                            {
                                                askForCurrentPassword
                                                    ?
                                                    <FormControl margin="normal" fullWidth>
                                                        <TextField
                                                            autoComplete="new-password"
                                                            label="Jelenlegi jelszó:"
                                                            type="password"
                                                            value={currentPassword || ""}
                                                            onChange={this.onEventBasedInputChange('currentPassword')}
                                                        />
                                                    </FormControl>
                                                    : ""
                                            }

                                            <Button
                                                disabled={!readyToSave}
                                                type="submit"
                                                fullWidth
                                                variant="contained"
                                                color="primary"
                                            >
                                                Mentés
                                            </Button>
                                        </form>
                                    </div>
                                )
                }
            </Paper>
        );
    }
}


export default withStyles(styles, {withTheme: true})(EditAccountView);