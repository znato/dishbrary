import React from "react";
import Typography from "@material-ui/core/es/Typography";
import FormControl from "@material-ui/core/FormControl";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import withStyles from "@material-ui/core/styles/withStyles";
import Avatar from '@material-ui/core/Avatar';
import DeleteIcon from "@material-ui/icons/Delete";
import IconButton from "@material-ui/core/IconButton";
import {Input, Paper} from "@material-ui/core";

import userService from "../../services/UserService";
import {LoadingState} from "../../services/constants/LoadingState";
import DishbraryProgress from "../general/DishbraryProgress";
import {isNotBlank, isBlank} from '../../services/utils/StringUtils';
import messagingService from "../../services/messaging/MessagingService";
import {eventType} from "../../config/MessageConstants";
import DishbraryAlertDialog from "../general/DishbraryAlertDialog";

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
    imageInput: {
        width: "0.1px",
        height: "0.1px",
        opacity: 0,
        overflow: "hidden",
        position: "absolute",
        zIndex: -1
    },
    profileImageInput: {
        margin: "auto",
        cursor: "pointer",
        padding: "0.625rem 1.25rem",
        width: theme.spacing.unit * 25,
        height: theme.spacing.unit * 25,
    },
    profileAvatar: {
        width: theme.spacing.unit * 25,
        height: theme.spacing.unit * 25,
    },
});

class EditAccountView extends React.Component {
    _isMounted = false;

    constructor(props) {
        super(props);

        this.state = {
            loadingState: LoadingState.none,
            currentPassword: null,
            errorMessage: null,
            passwordChangeIntended: false,
            confirmedPassword: null,
            dataEdited: false,
            selectedProfileImage: {},
            profileImageDeleted: false,
            user: {
                profileImageUrl: "",
                username: "",
                firstName: "",
                lastName: "",
                email: "",
                password: "",
            },
            alertData: {
                openAlert: false,
                alertDialogTitle: "",
                alertDialogContent: ""
            },
        }

        messagingService.subscribe(eventType.userLoggedIn, () => {
            if (this._isMounted) {
                this.fetchUserData();
            }
        });
    }

    componentDidMount() {
        this._isMounted = true;
        this.fetchUserData();
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    openAlertDialog = (title, message) => {
        this.setState(
            {
                alertData: {
                    openAlert: true,
                    alertDialogTitle: title,
                    alertDialogContent: message
                }
            })
    }

    closeAlertDialog = () => {
        this.setState(
            {
                alertData: {
                    openAlert: false,
                    alertDialogTitle: "",
                    alertDialogContent: ""
                }
            })
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

        if (isNotBlank(pwdText)) {
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

    saveUserData = (submitEvent) => {
        submitEvent.preventDefault();

        const {username, firstName, lastName, email, password} = this.state.user;

        const formData = new FormData(submitEvent.target);

        formData.set("username", username);
        formData.set("firstName", firstName);
        formData.set("lastName", lastName);
        formData.set("email", email);
        formData.set("password", password);
        formData.set("currentPassword", this.state.currentPassword);
        formData.set("profileImageDeleted", this.state.profileImageDeleted);

        userService.saveUserData(formData)
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.openAlertDialog("Hiba történt!", jsonResponse.message)
                } else {
                    this.openAlertDialog("", jsonResponse.content);
                }
            });
    }

    handleProfileImageChange = (event) => {
        event.preventDefault();

        const selectedFiles = event.target.files;

        if (selectedFiles && selectedFiles[0]) {
            let fileReader = new FileReader();

            const selectedProfileImage = {
                file: selectedFiles[0],
                imagePreviewUrl: fileReader.result
            };

            fileReader.onloadend = (event) => {
                selectedProfileImage.imagePreviewUrl = event.target.result;

                this.setState({selectedProfileImage});
            };

            fileReader.readAsDataURL(selectedProfileImage.file);

            this.setState({dataEdited: true});
        }
    }

    deleteProfileImage = () => {
        const {user} = this.state;

        let newUserState = user;
        newUserState.profileImageUrl = "";

        this.setState({
            dataEdited: true,
            user: newUserState,
            profileImageDeleted: true,
            selectedProfileImage: {}
        });
    }

    renderProfileAvatar = (user, selectedProfileImage, classes) => {
        const imagePreviewUrl = selectedProfileImage.imagePreviewUrl || user.profileImageUrl;

        if (isNotBlank(imagePreviewUrl)) {
            return (
                <React.Fragment>
                    <Avatar src={imagePreviewUrl} className={classes.profileAvatar} />

                    <IconButton onClick={this.deleteProfileImage} aria-label="delete">
                        <DeleteIcon />
                    </IconButton>
                </React.Fragment>
                )
        } else {
            return (
                <Avatar className={classes.profileAvatar}>
                    {user.username.charAt(0)}
                </Avatar>
            )
        }
    }

    render() {
        const {classes} = this.props;
        const {loadingState, user, dataEdited, selectedProfileImage, passwordChangeIntended, confirmedPassword, currentPassword, errorMessage, alertData} = this.state;

        const requiredFieldAreSet = isNotBlank(user.username) && isNotBlank(user.email);
        const formIsChangedAndComplete = requiredFieldAreSet && ((!passwordChangeIntended && dataEdited) || (passwordChangeIntended && user.password === confirmedPassword));

        const readyToSave = formIsChangedAndComplete && isNotBlank(currentPassword);

        return (
            <Paper className={classes.root}>
                <Typography component="h1" variant="h5">
                    Fiók szerkesztése
                </Typography>

                <DishbraryAlertDialog open={alertData.openAlert}
                                      dialogTitle={alertData.alertDialogTitle}
                                      dialogContent={alertData.alertDialogContent}
                                      onAlertDialogClose={this.closeAlertDialog}/>

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

                                            <FormControl margin="normal" fullWidth>
                                                <label
                                                    htmlFor="profileImageInput" className={classes.profileImageInput}>
                                                    {this.renderProfileAvatar(user, selectedProfileImage, classes)}
                                                </label>

                                               <Input id="profileImageInput"
                                                      name="profileImageInput"
                                                      className={classes.imageInput}
                                                      type="file"
                                                      accept="image/*"
                                                      onChange={this.handleProfileImageChange}/>
                                            </FormControl>

                                            <FormControl margin="normal" required fullWidth>
                                                <TextField
                                                    required
                                                    label="Felhasználónév:"
                                                    value={user.username || ""}
                                                    error={isBlank(user.username)}
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
                                                        required
                                                        error={user.password !== confirmedPassword}
                                                        autoComplete="new-password"
                                                        label="Új Jelszó megerősítése:"
                                                        type="password"
                                                        helperText={user.password !== confirmedPassword ? "A mező nem egyezik az új jelszóval!" : ""}
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
                                                    error={isBlank(user.email)}
                                                    onChange={this.onUserDataChange('email')}
                                                />
                                            </FormControl>

                                            {
                                                formIsChangedAndComplete
                                                    ?
                                                    <FormControl margin="normal" fullWidth>
                                                        <TextField
                                                            required
                                                            error={isBlank(currentPassword)}
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