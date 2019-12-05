import React from 'react';
import withStyles from '@material-ui/core/styles/withStyles';
import Button from "@material-ui/core/Button";
import FormControl from "@material-ui/core/FormControl";
import Typography from "@material-ui/core/es/Typography";

import UploadIcon from "../icons/UploadIcon";
import DishbraryAlertDialog from "../general/DishbraryAlertDialog";

import recipeService from '../../services/RecipeService';
import ReactPlayer from "react-player";
import DeleteIcon from '@material-ui/icons/Delete';
import IconButton from "@material-ui/core/IconButton";

const styles = theme => ({
    recipeVideoFileUploaderContainer: {
        "marginBottom": "2em"
    },
    form: {
        width: "80%",
        margin: "0 10% 0 10%",
    },
    videoPlayer: {
        margin: "auto",
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
            selectedVideo: props.selectedVideo || null,
            isModeEditing: props.selectedVideo ? true : false,
            newVideoSelected: false,
            videoDeleted: false,
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

        if (selectedFilesToUpload && selectedFilesToUpload[0]) {
            let fileReader = new FileReader();

            const fileData = {
                file: selectedFilesToUpload[0],
                videoPreviewUrl: fileReader.result
            }

            fileReader.onloadend = (event) => {
                fileData.videoPreviewUrl = event.target.result;

                this.setState({
                    selectedVideo: fileData
                });
            }

            fileReader.readAsDataURL(fileData.file);

            this.setState({newVideoSelected: true})
        }
    }

    removeVideo = () => {
        const {isModeEditing} = this.state;
        //in case we are in editing mode and video is removed we will show the save button instead of upload
        this.setState({
            videoDeleted: isModeEditing,
            selectedVideo: null
        });
    }

    deleteVideoFromServer = () => {
        const {recipeId} = this.props;

        recipeService.deleteRecipeVideo(recipeId)
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

    getVideoPreview = (selectedVideo) => {
        if (!selectedVideo) {
            return null;
        }

        const {classes} = this.props;

        return (
            <div id="video-preview-container">
                <Typography variant="subtitle2">
                    {selectedVideo.file.name}
                </Typography>

                <ReactPlayer className={classes.videoPlayer}
                             url={selectedVideo.videoPreviewUrl}
                             controls={true}/>

                <IconButton className={classes.deleteButton} onClick={this.removeVideo} aria-label="delete">
                    <DeleteIcon />
                </IconButton>
            </div>
        );
    }

    render() {
        const {recipeId, classes} = this.props;

        const {selectedVideo, newVideoSelected, videoDeleted, alertData} = this.state;

        const videoSelected = selectedVideo && selectedVideo.file && selectedVideo.videoPreviewUrl;

        let videoPreview = videoSelected ? this.getVideoPreview(selectedVideo) : null;

        return (
            <div id="recipeVideoFileUploaderContainer" className={classes.recipeVideoFileUploaderContainer}>
                <Typography variant="h6" className={classes.pageInfoText}>
                    Ezen a felületen lehetőséged van a recepthez videót feltölteni, amennyiben szeretnél. <br/>
                    A videó feltöltése nem kötlező! Ha nem kívánsz videót hozzáadni a receptedhez csak nyomj a "BEFEJEZ"
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

                        {videoPreview}

                        {


                            videoDeleted ?
                                <Button
                                    onClick={this.deleteVideoFromServer}
                                    fullWidth
                                    variant="contained"
                                    color="primary"
                                >
                                    Mentés
                                </Button>
                                :
                                <Button
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    color="primary"
                                    disabled={!(selectedVideo && newVideoSelected)}
                                >
                                    Feltöltés
                                </Button>
                        }
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