import React from 'react';
import withStyles from '@material-ui/core/styles/withStyles';
import InputLabel from '@material-ui/core/InputLabel/index';
import Button from "@material-ui/core/Button";
import FormControl from "@material-ui/core/FormControl";

import recipeService from '../../services/RecipeService';

const styles = theme => ({
    form: {
        width: "80%",
        margin: "0 10% 0 10%",
    },
    imagePreviewRadioGroupClass: {
        '&:checked + label>img': {
            "border": "1px solid #fff",
            "boxShadow": "0 0 3px 3px #090"
        },
        display: "none"
    },
    imagePreview: {
        width: "100px",
        height: "100px",
    }
});

class RecipeImageFileUploader extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            selectedFiles: [],
            selectedCoverImageFileName: null
        }
    }

    uploadRecipeImages = (recipeId) => (formSubmitEvent) => {
        formSubmitEvent.preventDefault();

        const formData = new FormData(formSubmitEvent.target);

        const {selectedCoverImageFileName} = this.state;

        recipeService.saveRecipeImages(recipeId, formData, {"selectedCoverImageFileName": selectedCoverImageFileName})
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    alert(jsonResponse.message);
                } else {
                    alert(jsonResponse.message);
                }
            });
    }

    handleImageInputChange = (event) => {
        event.preventDefault();

        let selectedFilesToUpload = event.target.files;

        //clearing selectedFiles array as we will rebuild it from the input component data
        this.state.selectedFiles = [];

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
    }

    handleCoverImageSelectionChange = (event) => {
        this.setState({
            selectedCoverImageFileName: event.target.value
        })
    }

    render() {
        const {recipeId, classes} = this.props;

        const {selectedFiles} = this.state;

        let $imagePreviews = [];
        //if the selectedFiles is not empty
        if (selectedFiles && selectedFiles[0]) {
            //set the first image as checked by default
            $imagePreviews.push(
                (

                    <label key={"selectedImageToUpload" + 0} className={classes.imagePreviewWrapper}>
                        <input id={"selectedImageToUpload" + 0} className={classes.imagePreviewRadioGroupClass}
                               type="radio" name="imagePreviewRadioGroup"
                               value={selectedFiles[0].file.name}
                               defaultChecked
                               onChange={this.handleCoverImageSelectionChange}/>
                        <label htmlFor={"selectedImageToUpload" + 0}>
                            <img src={selectedFiles[0].imagePreviewUrl} className={classes.imagePreview}/>
                        </label>
                    </label>
                )
            );

            for (let i = 1; i < selectedFiles.length; i++) {
                $imagePreviews.push(
                    (
                        <label key={"selectedImageToUpload" + i} className={classes.imagePreviewWrapper}>
                            <input id={"selectedImageToUpload" + i} className={classes.imagePreviewRadioGroupClass}
                                   type="radio" name="imagePreviewRadioGroup"
                                   value={selectedFiles[i].file.name}
                                   onChange={this.handleCoverImageSelectionChange}/>
                            <label htmlFor={"selectedImageToUpload" + i}>
                                <img src={selectedFiles[i].imagePreviewUrl} className={classes.imagePreview}/>
                            </label>
                        </label>
                    )
                );
            }
        }

        return (
            <div id="recipeImageFileUploaderContainer" className="recipeImageFileUploaderContainer">
                <form className={classes.form} onSubmit={this.uploadRecipeImages(recipeId)} autoComplete="off">
                    <FormControl margin="normal" required fullWidth>
                        <InputLabel htmlFor="imagesInput">Képek kiválasztása:</InputLabel>
                        <input id="imagesInput"
                               name="imagesInput"
                               type="file"
                               accept="image/*"
                               multiple
                               onChange={this.handleImageInputChange}/>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                        >
                            Feltőltés
                        </Button>
                    </FormControl>
                </form>

                <div id="recipeImageFileUploaderContainer-imagePreview"
                     className="recipeImageFileUploaderContainer-imagePreview">
                    {$imagePreviews}
                </div>
            </div>
        )
    }
}

export default withStyles(styles, {withTheme: true})(RecipeImageFileUploader);