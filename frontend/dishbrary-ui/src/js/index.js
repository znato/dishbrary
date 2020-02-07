import React from "react"
import ReactDOM from "react-dom"
import 'typeface-roboto';

import {HashRouter as Router, Route} from "react-router-dom";
import * as ApplicationRoutes from './config/ApplicationRoutes';

import DishbraryAppBar from './components/appbar/DishbraryAppBar';
import HomeView from './components/view/HomeView';
import CalorieTableView from './components/view/CalorieTableView';
import WhatIsInTheFridgeView from './components/view/WhatIsInTheFridgeView';
import MyRecipesView from './components/view/MyRecipesView';
import FavouriteRecipesView from './components/view/FavouriteRecipesView';
import CreateRecipeView from './components/view/CreateRecipeView';
import EditRecipeView from './components/view/EditRecipeView';
import RecipeView from './components/view/RecipeView';
import EditAccountView from './components/view/EditAccountView';

import userStatusChecker from "./services/UserStatusChecker";

function DishbraryApp() {
    return (
        <React.Fragment>
            <Router>
                <DishbraryAppBar/>

                <div id="dishbrary-page-content" style={{marginTop: "64px"}}>

                    <Route exact path={ApplicationRoutes.homePath} component={HomeView} />

                    <Route path={ApplicationRoutes.calorieTablePath} component={CalorieTableView} />

                    <Route path={ApplicationRoutes.whatIsInTheFridgePath} component={WhatIsInTheFridgeView} />

                    <Route path={ApplicationRoutes.userOwnRecipesPath} component={MyRecipesView}/>

                    <Route path={ApplicationRoutes.favouriteRecipesPath} component={FavouriteRecipesView}/>

                    <Route path={ApplicationRoutes.createRecipePath} component={CreateRecipeView}/>

                    <Route path={ApplicationRoutes.editRecipePath + "/:recipeId"} component={EditRecipeView}/>

                    <Route path={ApplicationRoutes.viewRecipePath + "/:recipeId"} component={RecipeView} />

                    <Route path={ApplicationRoutes.editAccountPath} component={EditAccountView} />
                </div>
            </Router>
        </React.Fragment>
    );
}

ReactDOM.render(<DishbraryApp/>, document.getElementById("app-root"));