import React from 'react';
import withStyles from '@material-ui/core/styles/withStyles';
import Button from "@material-ui/core/Button";
import IconButton from '@material-ui/core/IconButton';
import DeleteIcon from '@material-ui/icons/Delete';
import FormControl from "@material-ui/core/FormControl";
import Typography from "@material-ui/core/es/Typography";

import UploadIcon from "../icons/UploadIcon";
import DishbraryAlertDialog from "../general/DishbraryAlertDialog";

import recipeService from '../../services/RecipeService';
import * as ArrayUtils from '../../services/utils/ArrayUtils';

const styles = theme => ({
    recipeImageFileUploaderContainer: {
        "marginBottom": "2em"
    },
    form: {
        width: "80%",
        margin: "0 10% 0 10%",
    },
    imageInput: {
        "width": "0.1px",
        "height": "0.1px",
        "opacity": 0,
        "overflow": "hidden",
        "position": "absolute",
        "zIndex": -1
    },
    imageInputLabel: {
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
    coverImageInfoText: {
        "marginTop": "1em",
        "marginBottom": "2em"
    },
    selectImagesButton: {
        "marginBottom": "2em"
    },
    imagePreviewRadioGroupClass: {
        '&:checked + label>img': {
            "border": "1px solid #fff",
            "boxShadow": "0 0 3px 3px #090"
        },
        display: "none"
    },
    imagePreviewWrapper: {
        display: "inline-block",
    },
    imagePreview: {
        width: "100px",
        height: "100px",
        display: "block"
    }
});

class RecipeImageFileUploader extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            selectedFiles: props.selectedImages || [],
            isEditMode: props.selectedImages ? true : false,
            imageDataChanged: false,
            selectedCoverImageFileName: props.selectedCoverImageFileName,
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

    uploadRecipeImages = (recipeId) => {
        const formData = new FormData();

        const {selectedFiles, selectedCoverImageFileName} = this.state;

        formData.set("selectedCoverImageFileName", selectedCoverImageFileName);

        for (let i = 0; i < selectedFiles.length; i++) {
            formData.append("recipeImage", selectedFiles[i].file, selectedFiles[i].file.name);
        }

        recipeService.saveRecipeImages(recipeId, formData)
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.openAlertDialog("Hiba történt!", jsonResponse.message);
                } else {
                    //call additional callback if present
                    if (typeof this.props.onUploadSuccess === "function") {
                        this.props.onUploadSuccess({
                            selectedImages: selectedFiles,
                            selectedCoverImageFileName: selectedCoverImageFileName
                        });
                    }
                }
            });
    }

    deleteAllRecipeImages = (recipeId) => {
        recipeService.deleteAllRecipeImages(recipeId)
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.openAlertDialog("Hiba történt!", jsonResponse.message);
                } else {
                    //call additional callback if present
                    if (typeof this.props.onUploadSuccess === "function") {
                        this.props.onUploadSuccess({
                            selectedImages: [],
                            selectedCoverImageFileName: null
                        });
                    }
                }
            });
    }

    handleFormSubmit = (recipeId) => (formSubmitEvent) => {
        formSubmitEvent.preventDefault();

        const {isEditMode, selectedFiles, selectedCoverImageFileName} = this.state;

        const imagesRemoved = isEditMode && !selectedCoverImageFileName && ArrayUtils.isEmpty(selectedFiles);

        if (imagesRemoved) {
            this.deleteAllRecipeImages(recipeId);
        } else {
            this.uploadRecipeImages(recipeId);
        }
    }

    handleImageInputChange = (event) => {
        event.preventDefault();

        let selectedFilesToUpload = event.target.files;

        //to support 'back' button functionality
        //in case user goes back to imageUpload it is possible to re-select only the coverImage and press save
        //in this case selectedFilesToUpload will be empty but we already have the file data to upload
        if (ArrayUtils.isEmpty(selectedFilesToUpload) && ArrayUtils.isNotEmpty(this.state.selectedFiles)) {
            return;
        }

        //clearing selectedFiles array as we will rebuild it from the input component data
        this.state.selectedFiles = [];
        this.state.selectedCoverImageFileName = null

        for (let i = 0; i < selectedFilesToUpload.length; i++) {
            let fileReader = new FileReader();

            const fileData = {
                file: selectedFilesToUpload[i],
                imagePreviewUrl: fileReader.result
            }

            fileReader.onloadend = (event) => {
                fileData.imagePreviewUrl = event.target.result;

                this.setState({
                    selectedFiles: [...this.state.selectedFiles, fileData]
                });
            }

            fileReader.readAsDataURL(fileData.file);
        }

        this.setState({imageDataChanged: true})
    }

    handleCoverImageSelectionChange = (event) => {
        this.setState({
            imageDataChanged: true,
            selectedCoverImageFileName: event.target.value
        })
    }

    deleteImage = imageName => () => {
        let {selectedFiles,selectedCoverImageFileName} = this.state;

        let resetCoverImageFileName = false;

        for (let i = 0; i < selectedFiles.length; i++) {
            let actualFileData = selectedFiles[i].file;

            if (actualFileData.name === imageName) {
                if (actualFileData.name === selectedCoverImageFileName) {
                    resetCoverImageFileName = true;
                }

                selectedFiles.splice(i, 1);
                break;
            }
        }

        let stateUpdate = resetCoverImageFileName && ArrayUtils.isNotEmpty(selectedFiles) ? {
            imageDataChanged: true,
            selectedFiles,
            selectedCoverImageFileName: selectedFiles[0].file.name
        } : {
            imageDataChanged: true,
            selectedFiles,
            selectedCoverImageFileName: null
        };

        this.setState(stateUpdate);
    }

    render() {
        const {recipeId, classes} = this.props;

        const {selectedFiles, alertData, selectedCoverImageFileName, imageDataChanged, isEditMode} = this.state;

        let imagePreviewInformation = null;

        let $imagePreviews = [];

        let filesAreSelected = selectedFiles && selectedFiles[0];

        if (filesAreSelected) {
            imagePreviewInformation =
                <Typography variant="subtitle2" className={classes.coverImageInfoText}>A feltölteni kívant képek közül a kiválasztott lesz a recept
                    borítóképe. Alapértelmezetten az első kép!</Typography>;

            //set the first image as checked by default
            if (selectedCoverImageFileName === null) {
                this.state.selectedCoverImageFileName = selectedFiles[0].file.name;
            }

            for (let i = 0; i < selectedFiles.length; i++) {
                let imageName = selectedFiles[i].file.name;
                $imagePreviews.push(
                    (
                        <label key={"selectedImageToUpload" + i} className={classes.imagePreviewWrapper}>
                            <input id={"selectedImageToUpload" + i} className={classes.imagePreviewRadioGroupClass}
                                   type="radio" name="imagePreviewRadioGroup"
                                   value={imageName}
                                   checked={selectedCoverImageFileName === imageName}
                                   onChange={this.handleCoverImageSelectionChange}/>
                            <label htmlFor={"selectedImageToUpload" + i}>
                                <img src={selectedFiles[i].imagePreviewUrl} className={classes.imagePreview}/>
                            </label>
                            <IconButton className={classes.deleteButton} onClick={this.deleteImage(imageName)} aria-label="delete">
                                <DeleteIcon />
                            </IconButton>
                        </label>
                    )
                );
            }
        }

        return (
            <div id="recipeImageFileUploaderContainer" className={classes.recipeImageFileUploaderContainer}>
                <Typography variant="h6" className={classes.pageInfoText}>
                    Ezen a felületen lehetőséged van a recepthez képeket feltölteni, amennyiben szeretnél. <br/>
                    A képek feltöltése nem kötlező! Ha nem kívánsz képeket hozzáadni a receptedhez csak nyomj a "TOVÁBB" gombra.
                </Typography>

                <form className={classes.form} onSubmit={this.handleFormSubmit(recipeId)} autoComplete="off">
                    <FormControl margin="normal" required fullWidth>
                        <div className={classes.selectImagesButton}>
                            <label
                                htmlFor="imagesInput" className={classes.imageInputLabel}>
                                <UploadIcon/>
                                Képek kiválasztása
                            </label>
                            <input id="imagesInput"
                                   name="imagesInput"
                                   type="file"
                                   accept="image/*"
                                   multiple
                                   onChange={this.handleImageInputChange}
                                   className={classes.imageInput}/>
                        </div>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                            disabled={ ! ((!isEditMode && filesAreSelected) || (isEditMode && imageDataChanged)) }
                        >
                            {isEditMode ? "Mentés" : "Feltőltés"}
                        </Button>
                    </FormControl>
                </form>

                {imagePreviewInformation}

                <div id="recipeImageFileUploaderContainer-imagePreview"
                     className="recipeImageFileUploaderContainer-imagePreview">
                    {$imagePreviews}
                </div>

                <DishbraryAlertDialog open={alertData.openAlert}
                                      dialogTitle={alertData.alertDialogTitle}
                                      dialogContent={alertData.alertDialogContent}
                                      onAlertDialogClose={this.closeAlertDialog}/>
            </div>
        )
    }
}

export default withStyles(styles, {withTheme: true})(RecipeImageFileUploader);