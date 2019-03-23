import React from "react"

export class Banner extends React.Component {
    render() {
        return (
            <div className="cookbook-banner">
                <img src={this.props.image}/>
            </div>
        )
    }
}

export class CookbookButton extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <a className="cookbook-button">{this.props.text}</a>
        )
    }
}