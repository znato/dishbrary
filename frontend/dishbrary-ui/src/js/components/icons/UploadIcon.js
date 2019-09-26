import React from 'react';
import SvgIcon from '@material-ui/core/SvgIcon';
import withStyles from '@material-ui/core/styles/withStyles';

const styles = {
    icon: {
        "fontSize": "27px",
        "verticalAlign": "middle",
        "marginTop": "-0.15em",
        "marginRight": "0.25em"
    },
};

const UploadIcon = (props) => {
    const {classes} = props;

    return (
        <SvgIcon className={classes.icon} width="24" height="24" viewBox="0 0 24 24">
            <path d="M9 16h6v-6h4l-7-7-7 7h4zm-4 2h14v2H5z"/>
        </SvgIcon>
    );
}

export default withStyles(styles)(UploadIcon);