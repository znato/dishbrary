import React from "react"
import ReactDOM from "react-dom"

import DishbraryAppBar from './components/DishBraryAppBar';

function DishbraryApp() {
    return (
        <React.Fragment>
            <DishbraryAppBar/>
        </React.Fragment>
    );
}

ReactDOM.render(<DishbraryApp/>, document.getElementById("app-root"));