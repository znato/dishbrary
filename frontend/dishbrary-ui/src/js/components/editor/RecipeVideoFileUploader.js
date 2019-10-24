import React from 'react';
import withStyles from '@material-ui/core/styles/withStyles';
import Button from "@material-ui/core/Button";
import FormControl from "@material-ui/core/FormControl";
import Typography from "@material-ui/core/es/Typography";

import UploadIcon from "../icons/UploadIcon";
import DishbraryAlertDialog from "../general/DishbraryAlertDialog";

import recipeService from '../../services/RecipeService';

const styles = theme => ({
    recipeVideoFileUploaderContainer: {
        "marginBottom": "2em"
    },
    form: {
        width: "80%",
        margin: "0 10% 0 10%",
    },
    videoInput: {
        "width": "0.1px",
        "height": "0.1px",
        "opacity": 0,
        "overflow": "hidden",
        "position": "absolute",
        "zIndex": -1
    },
    videoInputLabel: {
        "maxWidth": "80%",
        "fontSize": "1.25rem",
        "fontWeight": 700,
        "textOverflow": "ellipsis",
        "whiteSpace": "nowrap",
        "cursor": "pointer",
        "display": "inline-block",
        "overflow": "hidden",
        "padding": "0.625rem 1.25rem",
        "color": "#f1e5e6",
        "backgroundColor": "#d3394c"
    },
    pageInfoText: {
        "marginTop": "2em",
        "marginBottom": "1em"
    },
    selectVideoButton: {
        "marginBottom": "2em"
    }
});

class RecipeVideoFileUploader extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            videoSelected: false,
            alertData: {
                openAlert: false,
                alertDialogTitle: "",
                alertDialogContent: ""
            }
        }

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

    uploadRecipeVideo = (recipeId) => (formSubmitEvent) => {
        formSubmitEvent.preventDefault();

        const formData = new FormData(formSubmitEvent.target);

        recipeService.saveRecipeVideo(recipeId, formData)
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.openAlertDialog("Hiba történt!", jsonResponse.message);
                } else {
                    //call additional callback if present
                    if (typeof this.props.onUploadSuccess === "function") {
                        this.props.onUploadSuccess();
                    }
                }
            });
    }

    handleVideoInputChange = (event) => {
        event.preventDefault();

        let selectedFilesToUpload = event.target.files;

        this.setState({videoSelected: selectedFilesToUpload && selectedFilesToUpload[0]})
    }

    render() {
        const {recipeId, classes} = this.props;

        const {videoSelected, alertData} = this.state;

        return (
            <div id="recipeVideoFileUploaderContainer" className={classes.recipeVideoFileUploaderContainer}>
                <Typography variant="h6" className={classes.pageInfoText}>
                    Ezen a felületen lehetőséged van a recepthez videót feltölteni, amennyiben szeretnél. <br/>
                    A videó feltöltése nem kötlező! Ha nem kívánsz videót hozzáadni a receptedhez csak nyomj "BEFEJEZ"
                    gombra.
                </Typography>

                <form className={classes.form} onSubmit={this.uploadRecipeVideo(recipeId)} autoComplete="off">
                    <FormControl margin="normal" required fullWidth>
                        <div className={classes.selectVideoButton}>
                            <label
                                htmlFor="videoInput" className={classes.videoInputLabel}>
                                <UploadIcon/>
                                Videó kiválasztása
                            </label>
                            <input id="videoInput"
                                   name="videoInput"
                                   type="file"
                                   accept="video/mp4,video/webm,video/ogg"
                                   onChange={this.handleVideoInputChange}
                                   className={classes.videoInput}/>
                        </div>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            disabled={!videoSelected}
                        >
                            Feltőltés
                        </Button>
                    </FormControl>
                </form>

                <DishbraryAlertDialog open={alertData.openAlert}
                                      dialogTitle={alertData.alertDialogTitle}
                                      dialogContent={alertData.alertDialogContent}
                                      onAlertDialogClose={this.closeAlertDialog}/>
            </div>
        )
    }
}

export default withStyles(styles, {withTheme: true})(RecipeVideoFileUploader);