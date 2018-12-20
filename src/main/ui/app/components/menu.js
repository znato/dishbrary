class CookbookMenuItem extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <a className="cookbook-menu-item">{this.props.title}</a>
        )
    }
}

class CookbookDropdownMenuItem extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            elementsInfo: []
        }
    }

    componentDidMount() {
        if (this.props.staticElementInfos) {
            this.setState({elementsInfo : this.props.staticElementInfos})
        } else {
            this.fetchUrl(this.props.elementsInfoUrl);
        }
    }

    fetchUrl(url) {
        fetch(url)
            .then(response => response.json())
            .then(result => this.setState({elementsInfo : result.body}))
    }

    render() {
        return (
            <div className="cookbook-menu-item cookbook-dropdown-menu-item">
                <a className="parent-item">{this.props.title}
                    <span className="caret-down"></span>
                </a>
                <div className="cookbook-dropdown-content">
                    {
                        this.state.elementsInfo.map((elementInfo, index) => {
                            return <a>{elementInfo.name}</a>
                        })
                    }
                </div>
            </div>
        )
    }
}

class CookbookSearchBox extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="cookbook-search-box">
                <input type="text" placeholder="Keresés..."/>
                <a><i className="fa fa-search"></i></a>
            </div>
        )
    }
}

class MainMenu extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            categoriesElementInfo : []
        }
    }

    render() {
        return (
            <div className="main-menu">
                <CookbookMenuItem title="Receptek"/>
                <CookbookDropdownMenuItem title="Kategóriák" elementsInfoUrl="/rest/recipe/category/all"/>
                <CookbookMenuItem title="Mi van a hűtőben?"/>

                <CookbookSearchBox/>
            </div>
        )
    }
}