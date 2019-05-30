import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';
import Paper from '@material-ui/core/Paper';
import CircularProgress from '@material-ui/core/CircularProgress';
import Typography from "@material-ui/core/es/Typography/Typography";

import {ReactMUIDatatable} from "react-material-ui-datatable";

import ingredientService from '../../services/IngredientService';
import {LoadingState} from '../../services/constants/LoadingState';

const styles = theme => ({
    root: {
        width: '100%',
        marginTop: theme.spacing.unit * 3,
        overflowX: 'auto',
        textAlign: "center",
    },
    table: {
        minWidth: 700,
    },
    tableRow: {
        '&:nth-of-type(odd)': {
            backgroundColor: theme.palette.background.default,
        },
    },
    progress: {
        margin: theme.spacing.unit * 2,
    }
});

const columns = [
    {
        name: "name",
        label: "Név"
    },
    {
        name: "energyKcal",
        label: "Kalória (Kcal)"
    },
    {
        name: "protein",
        label: "Fehérje (g)"
    },
    {
        name: "fat",
        label: "Zsír (g)"
    },
    {
        name: "carbohydrate",
        label: "Szénhidrát (g)"
    }
];

class CalorieTableView extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            loading: LoadingState.none,
            ingredientList: []
        };
    }

    componentDidMount() {
        this.fetchAllIngredient();
    }

    fetchAllIngredient = () => {
        this.setState({loading: LoadingState.inProgress});

        ingredientService.getAllIngredient()
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({loading: LoadingState.error});
                } else {
                    this.setState({
                        loading: LoadingState.loaded,
                        ingredientList: jsonResponse.content
                    });
                }
            })
    }

    render() {
        const {classes} = this.props;
        const {loading, ingredientList} = this.state;

        return (
            <Paper className={classes.root}>
                {
                    loading === LoadingState.inProgress
                        ?
                        <CircularProgress disableShrink={true} className={classes.progress}/>
                        :
                        loading === LoadingState.none
                            ?
                            ""
                            :
                            loading === LoadingState.error ?
                                <Typography>Az oldal jelenleg nem elérhető! Kérjük próbálja később!</Typography>
                                :
                                (
                                    <ReactMUIDatatable columns={columns} data={ingredientList}
                                                       perPage={10}
                                                       perPageOption={[10, 25, 50, 100]}
                                                       sort={{columnName: "name", direction: "ASC"}}
                                                       selectable={false}
                                                       filterable={false}/>
                                )
                }

            </Paper>
        );
    }
}

export default withStyles(styles)(CalorieTableView);
