import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';

class DishbraryAlertDialog extends React.Component {

    constructor(props) {
        super(props);
    }

    closeAlertDialog = () => {
        if (typeof this.props.onAlertDialogClose === "function") {
            this.props.onAlertDialogClose();
        }
    }

    render() {
        const {open, dialogTitle, dialogContent} = this.props;

        return (
            <div>
                <Dialog
                    open={open}
                    onClose={this.closeAlertDialog}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">{dialogTitle}</DialogTitle>
                    <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                            {dialogContent}
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.closeAlertDialog} color="primary">
                            OK
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}

export default DishbraryAlertDialog;