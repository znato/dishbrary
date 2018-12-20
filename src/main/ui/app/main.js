function CookbookApp() {
    return (
        <div>
            <Banner bannerImageUrl="/images/cookbook-banner.jpg"/>
            <MainMenu/>
        </div>
    );
}

ReactDOM.render(<CookbookApp/>, document.getElementById("app-root"));