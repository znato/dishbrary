import React from "react"
import ReactDOM from "react-dom"
import 'typeface-roboto';

import DishbraryAppBar from './components/appbar/DishbraryAppBar';

function DishbraryApp() {
    return (
        <React.Fragment>
            <DishbraryAppBar/>
        </React.Fragment>
    );
}

ReactDOM.render(<DishbraryApp/>, document.getElementById("app-root"));