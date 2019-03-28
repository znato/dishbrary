import React from "react"

export class DishbraryMenuItem extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <a className="dishbrary-menu-item">{this.props.title}</a>
        )
    }
}

export class DishbraryDropdownMenuItem extends React.Component {
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
            <div className="dishbrary-menu-item dishbrary-dropdown-menu-item">
                <a className="parent-item">{this.props.title}
                    <span className="caret-down"></span>
                </a>
                <div className="dishbrary-dropdown-content">
                    {
                        this.state.elementsInfo.map((elementInfo) => {
                            return <a key={elementInfo.id}>{elementInfo.name}</a>
                        })
                    }
                </div>
            </div>
        )
    }
}

export class DishbrarySearchBox extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="dishbrary-search-box">
                <input type="text" placeholder="Keresés..."/>
                <a><i className="fa fa-search"></i></a>
            </div>
        )
    }
}

export class MainMenu extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            categoriesElementInfo : []
        }
    }

    render() {
        return (
            <div className="main-menu">
                <DishbraryMenuItem title="Receptek"/>
                <DishbraryDropdownMenuItem title="Kategóriák" elementsInfoUrl="/rest/recipe/category/all"/>
                <DishbraryMenuItem title="Mi van a hűtőben?"/>

                <DishbrarySearchBox/>
            </div>
        )
    }
}