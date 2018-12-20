class Banner extends React.Component {
    render() {
        return (
            <div className="cookbook-banner">
                <img src={this.props.bannerImageUrl}/>
            </div>
        )
    }
}

class CookbookButton extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <a className="cookbook-button">{this.props.text}</a>
        )
    }
}
