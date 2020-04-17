import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';

class DishbraryConfirmDialog extends React.Component {

    constructor(props) {
        super(props);
    }

    onActionNo = () => {
        if (typeof this.props.onActionNo === "function") {
            this.props.onActionNo();
        }
    }

    onActionYes = () => {
        if (typeof this.props.onActionYes === "function") {
            this.props.onActionYes();
        }
    }

    render() {
        const {open, dialogTitle, dialogContent} = this.props;

        return (
            <div>
                <Dialog
                    open={open}
                    onClose={this.onActionNo}
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
                        <Button onClick={this.onActionYes} color="secondary">
                            Igen
                        </Button>

                        <Button onClick={this.onActionNo} color="primary">
                            Nem
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}

export default DishbraryConfirmDialog;