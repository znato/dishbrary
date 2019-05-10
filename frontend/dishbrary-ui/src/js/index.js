import React from "react"
import ReactDOM from "react-dom"
import 'typeface-roboto';

import {HashRouter as Router, Route} from "react-router-dom";
import * as ApplicationRoutes from './config/ApplicationRoutes';

import DishbraryAppBar from './components/appbar/DishbraryAppBar';
import CalorieTableView from './components/view/CalorieTableView';

function DishbraryApp() {
    return (
        <React.Fragment>
            <Router>
                <DishbraryAppBar/>

                <div id="dishbrary-page-content" style={{marginTop: "70px"}}>
                    <Route path={ApplicationRoutes.calorieTablePath} component={CalorieTableView} />
                </div>
            </Router>
        </React.Fragment>
    );
}

ReactDOM.render(<DishbraryApp/>, document.getElementById("app-root"));