const HtmlWebPackPlugin = require("html-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = {
    entry: {
        app: "./src/js/index.js"
    },
    output: {
        path: __dirname + "/../target/dist/static"
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: {
                    loader: "babel-loader",
                    options: {
                        presets: [
                            "@babel/preset-env",
                            "@babel/preset-react"
                        ],
                        plugins: [
                            "@babel/plugin-proposal-class-properties"
                        ]
                    }
                }
            },
            {
                // rule for pure CSS (without CSS modules)
                test: /\.pure\.css$|(carousel.min.css|main.min.css)$/i,
                use: [MiniCssExtractPlugin.loader, 'css-loader'],
            },
        ]
    },
    plugins: [
        new HtmlWebPackPlugin({
            template: "src/index.html",
            filename: "index.html"
        }),
        new MiniCssExtractPlugin({
            filename: "[name].css",
            chunkFilename: "[id].css"
        })
    ]

};
