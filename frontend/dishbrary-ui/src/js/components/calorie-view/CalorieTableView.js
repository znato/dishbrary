import React from 'react';

import withStyles from '@material-ui/core/styles/withStyles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import CircularProgress from '@material-ui/core/CircularProgress';
import Typography from "@material-ui/core/es/Typography/Typography";

import ingredientService from '../../services/IngredientService';

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

const loadingState = {
    none: "none",
    inProgress: "inProgress",
    loaded: "loaded",
    error: "error"
}

class CalorieTableView extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            loading: loadingState.none,
            ingredientList: []
        };
    }

    componentDidMount() {
        this.fetchAllIngredient();
    }

    fetchAllIngredient = () => {
        this.setState({loading: loadingState.inProgress});

        ingredientService.getAllIngredient()
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({loading: loadingState.error});
                } else {
                    this.setState({
                        loading: loadingState.loaded,
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
                    loading === loadingState.inProgress
                        ?
                        <CircularProgress disableShrink={true} className={classes.progress}/>
                        :
                        loading === loadingState.none
                            ?
                            ""
                            :
                            loading === loadingState.error ? <Typography>Az oldal jelenleg nem elérhető! Kérjük próbálja később!</Typography>
                                :
                                (
                                    <Table className={classes.table}>
                                        <TableHead>
                                            <TableRow>
                                                <TableCell>Név</TableCell>
                                                <TableCell align="right">Kalória (Kcal)</TableCell>
                                                <TableCell align="right">Fehérje (g)</TableCell>
                                                <TableCell align="right">Zsír (g)</TableCell>
                                                <TableCell align="right">Szénhidrát (g)</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {
                                                ingredientList.map(ingredient => (
                                                    <TableRow className={classes.tableRow} key={ingredient.id}>
                                                        <TableCell component="th" scope="row">
                                                            {ingredient.name}
                                                        </TableCell>
                                                        <TableCell align="right">{ingredient.energyKcal}</TableCell>
                                                        <TableCell align="right">{ingredient.protein}</TableCell>
                                                        <TableCell align="right">{ingredient.fat}</TableCell>
                                                        <TableCell align="right">{ingredient.carbohydrate}</TableCell>
                                                    </TableRow>
                                                ))
                                            }
                                        </TableBody>
                                    </Table>
                                )
                }

            </Paper>
        );
    }
}

export default withStyles(styles)(CalorieTableView);
