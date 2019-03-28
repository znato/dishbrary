import React from "react"

export class Banner extends React.Component {
    render() {
        return (
            <div className="dishbrary-banner">
                <img src={this.props.image}/>
            </div>
        )
    }
}

export class DishbraryButton extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <a className="dishbrary-button">{this.props.text}</a>
        )
    }
}