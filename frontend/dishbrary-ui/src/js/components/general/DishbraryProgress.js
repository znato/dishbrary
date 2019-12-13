import React from 'react';
import withStyles from '@material-ui/core/styles/withStyles';
import CircularProgress from "@material-ui/core/CircularProgress";

const styles = theme => ({
    progress: {
        top: '50%',
        position: 'absolute',
        left: '47%'
    }
});

class DishbraryProgress extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        const {classes} = this.props;

        return (
            <CircularProgress size={100} disableShrink={true} className={classes.progress}/>
        );
    }
}

export default withStyles(styles)(DishbraryProgress);