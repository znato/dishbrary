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
            <span className="cookbook-button">{this.props.text}</span>
        )
    }
}

class MainMenu extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            categories : []
        }
    }

    componentDidMount() {
        this.fetchCategories();
    }

    fetchCategories() {
        fetch("/rest/recipe/category/all")
            .then(response => response.json())
            .then(result => this.setState({categories : result.body}))
    }

    render() {
        return (
            <ul>
                {
                    this.state.categories.map(category => {
                        return <li><CookbookButton text={category.name}/></li>
                    })
                }
            </ul>
        )
    }
}