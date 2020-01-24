import React from 'react';
import withStyles from '@material-ui/core/styles/withStyles';

import UpArrow from '@material-ui/icons/KeyboardArrowUp';
import DownArrow from '@material-ui/icons/KeyboardArrowDown';
import TextField from '@material-ui/core/TextField';
import Collapse from "@material-ui/core/Collapse";
import Typography from "@material-ui/core/es/Typography";
import CircularProgress from "@material-ui/core/CircularProgress";

import SuggestionSelect from "../editor/SuggestionSelect";
import {LoadingState, LoadingStateByIndex} from "../../services/constants/LoadingState";

import categoryService from "../../services/CategoryService";
import ingredientService from "../../services/IngredientService";
import cuisineService from "../../services/CuisineService";

import * as ArrayUtils from '../../services/utils/ArrayUtils';

const styles = theme => ({
    searchComponentContainer: {
        margin: "15px 0 30px 0",
    },
    searchField: {
        width: '90%',
    },
    advancedSearchCircularProgress: {
        marginTop: '10px',
        marginBottom: '10px'
    },
    searchFieldAdornmentIcon: {
        cursor: 'pointer'
    },
    advancedSearchContainerCollapse: {
        position: 'absolute',
        borderColor: 'rgba(0, 0, 0, 0.23)',
        borderStyle: 'solid',
        borderWidth: '1px',
        borderRadius: '4px',
        background: 'white',
        zIndex: 1000,
        width: '90%',
        left: '5%',
    },
    advancedSearchContainer: {
        padding: '30px 30px 45px 30px',
    },
});

class DishbraryRecipeSearch extends React.Component {

    constructor(props) {
        super(props);

        const {searchCriteria} = props;

        let passedSearchText = null;
        let passedIngredientSelection;
        let passedCuisineSelection;
        let passedCategorySelection;

        if (searchCriteria) {
            passedSearchText = searchCriteria.plainTextSearch;

            if (ArrayUtils.isNotEmpty(searchCriteria.ingredientList)) {
                passedIngredientSelection = searchCriteria.ingredientList.map(data => {
                    return {
                        value: data.id,
                        label: data.name
                    }
                });
            }

            if (ArrayUtils.isNotEmpty(searchCriteria.cuisineList)) {
                passedCuisineSelection = searchCriteria.cuisineList.map(data => {
                    return {
                        value: data.id,
                        label: data.name
                    }
                });
            }

            if (ArrayUtils.isNotEmpty(searchCriteria.categoryList)) {
                passedCategorySelection = searchCriteria.categoryList.map(data => {
                    return {
                        value: data.id,
                        label: data.name
                    }
                });
            }
        }

        this.state = {
            advancedSearchOptionsOpen: false,
            searchText: passedSearchText,
            ingredients: [],
            selectedIngredients: passedIngredientSelection || [],
            categories: [],
            selectedCategories: passedCategorySelection || [],
            cuisines: [],
            selectedCuisines: passedCuisineSelection || [],
            categoriesLoading: LoadingState.none,
            ingredientsLoading: LoadingState.none,
            cuisinesLoading: LoadingState.none,
        }
    }

    toggleAdvancedSearch = () => {
        const {advancedSearchOptionsOpen} = this.state;

        if (!advancedSearchOptionsOpen) {
            this.fetchAdvancedSearchDataIfNotAvailable();
        }

        this.setState({advancedSearchOptionsOpen: !advancedSearchOptionsOpen})
    }

    fetchAdvancedSearchDataIfNotAvailable = () => {
        const {categoriesLoading, ingredientsLoading, cuisinesLoading} = this.state;

        if (categoriesLoading ===  LoadingState.loaded &&
            ingredientsLoading ===  LoadingState.loaded &&
            cuisinesLoading === LoadingState.loaded) {
            return;
        }

        this.setState({
            categoriesLoading: LoadingState.inProgress,
            ingredientsLoading: LoadingState.inProgress,
            cuisinesLoading: LoadingState.inProgress,
        });

        categoryService.getAllCategories()
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({categoriesLoading: LoadingState.error});
                } else {
                    this.setState({
                        categoriesLoading: LoadingState.loaded,
                        categories: jsonResponse.content.map(category => ({
                            value: category.id,
                            label: category.name
                        }))
                    });
                }
            });

        ingredientService.getAllIngredient()
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({ingredientsLoading: LoadingState.error});
                } else {
                    this.setState({
                        ingredientsLoading: LoadingState.loaded,
                        ingredients: jsonResponse.content.map(ingredient => ({
                            value: ingredient.id,
                            label: ingredient.name
                        }))
                    });
                }
            });

        cuisineService.getAllCuisines()
            .then(jsonResponse => {
                if (jsonResponse.error) {
                    this.setState({cuisinesLoading: LoadingState.error});
                } else {
                    this.setState({
                        cuisinesLoading: LoadingState.loaded,
                        cuisines: jsonResponse.content.map(cuisine => ({
                            value: cuisine.id,
                            label: cuisine.name
                        }))
                    });
                }
            });
    }

    handleInputChange = name => value => {
        this.setState({
            [name]: value,
        });
    };

    handleSearchTextChange = (changeEvent) => {
        this.setState({searchText: changeEvent.target.value});
    }

    triggerSearch = () => {
        if (typeof this.props.onSearchTrigger === 'function') {
            const {searchText, selectedIngredients, selectedCategories, selectedCuisines} = this.state;

            const searchCriteria = {
                plainTextSearch: searchText !== null && searchText.length === 0 ? null : searchText,
                ingredientList: ArrayUtils.isEmpty(selectedIngredients) ? null : selectedIngredients.map(category => {
                    return {
                        id: category.value,
                        name: category.label
                    };
                }),
                categoryList: ArrayUtils.isEmpty(selectedCategories) ? null : selectedCategories.map(category => {
                    return {
                        id: category.value,
                        name: category.label
                    };
                }),
                cuisineList: ArrayUtils.isEmpty(selectedCuisines) ? null : selectedCuisines.map(category => {
                    return {
                        id: category.value,
                        name: category.label
                    };
                })
            }

            this.props.onSearchTrigger(searchCriteria);
        }
    }

    render() {
        const {classes} = this.props;
        const {advancedSearchOptionsOpen,
            categoriesLoading, ingredientsLoading, cuisinesLoading,
            ingredients, categories, cuisines,
            searchText, selectedIngredients, selectedCategories, selectedCuisines} = this.state;

        const highestLoadingStateIndex = Math.max(categoriesLoading.index, ingredientsLoading.index, cuisinesLoading.index);
        const overallLoadingState = LoadingStateByIndex[highestLoadingStateIndex];

        const searchFieldAdornmentIcon =  advancedSearchOptionsOpen
                ? <UpArrow onClick={this.toggleAdvancedSearch} className={classes.searchFieldAdornmentIcon}/>
                : <DownArrow onClick={this.toggleAdvancedSearch} className={classes.searchFieldAdornmentIcon}/> ;

        return (
            <div className={classes.searchComponentContainer}>
                <TextField className={classes.searchField} label="Keresés..." variant="outlined" value={searchText}
                           InputProps={{
                               endAdornment: (
                                   searchFieldAdornmentIcon
                               ),
                           }}
                           onChange={this.handleSearchTextChange}
                           onKeyPress={(ev) => {
                               if (ev.key === 'Enter') {
                                   ev.preventDefault();
                                   this.triggerSearch();
                               }
                           }}/>

                <Collapse className={classes.advancedSearchContainerCollapse} in={advancedSearchOptionsOpen}>
                    <div className={classes.advancedSearchContainer}>
                        {
                            overallLoadingState === LoadingState.inProgress
                                ?
                                <CircularProgress className={classes.advancedSearchCircularProgress} disableShrink={true}/>
                                :
                                overallLoadingState === LoadingState.none
                                    ?
                                    ""
                                    :
                                    overallLoadingState === LoadingState.error ?
                                        <Typography>A további szűrési opciók jelenleg nem elérhetőek! Kérjük próbálja
                                            később!</Typography>
                                        :
                                        (
                                            <React.Fragment>
                                                <SuggestionSelect label={"Hozzávalók:"}
                                                                  placeholder="Szűrj hozzávalókra"
                                                                  suggestions={ingredients}
                                                                  value={selectedIngredients}
                                                                  multiSelect
                                                                  onValueChange={this.handleInputChange('selectedIngredients')}/>

                                                <SuggestionSelect label={"Kategóriák:"}
                                                                  placeholder="Szűrj kategóriákra"
                                                                  suggestions={categories}
                                                                  value={selectedCategories}
                                                                  multiSelect
                                                                  onValueChange={this.handleInputChange('selectedCategories')}/>

                                                <SuggestionSelect label={"Konyha nemzetisége:"}
                                                                  placeholder="Szűrj a konyha nemzetiségére"
                                                                  suggestions={cuisines}
                                                                  value={selectedCuisines}
                                                                  multiSelect
                                                                  onValueChange={this.handleInputChange('selectedCuisines')}/>
                                            </React.Fragment>
                                        )
                        }
                    </div>
                </Collapse>
            </div>
        )
    }
}

export default withStyles(styles)(DishbraryRecipeSearch);