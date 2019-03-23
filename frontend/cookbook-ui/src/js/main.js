import React from "react"
import ReactDOM from "react-dom"

import 'font-awesome/css/font-awesome.min.css';
import "../css/menu.css"

import bannerImage from "../images/cookbook-banner.jpg"
import {Banner} from "./components/common-components"
import {MainMenu} from "./components/menu"

function CookbookApp() {
    return (
        <div>
            <Banner image={bannerImage} />
            <MainMenu/>
        </div>
    );
}

ReactDOM.render(<CookbookApp/>, document.getElementById("app-root"));