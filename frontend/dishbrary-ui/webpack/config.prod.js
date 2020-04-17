
const merge = require("webpack-merge");
const baseConfig = require("./config.base.js");

const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = merge(baseConfig, {
    mode: "production",
    module: {
        rules: [
            {
                test: /\.html$/,
                use: "html-loader"
            },
            {
                // rule for CSS modules
                test: /\.css$/i,
                exclude: /\.pure\.css$|(carousel.min.css)$/i,
                use: [
                    MiniCssExtractPlugin.loader,
                    {
                        loader: "css-loader",
                        options: {
                            modules: true,
                            importLoaders: 1
                        }
                    },
                ],
            },
            {
                test: /\.(woff(2)?|ttf|eot)(\?v=\d+\.\d+\.\d+)?$/,
                use: [
                    {
                        loader: "file-loader",
                        options: {
                            name: '[hash:10].[ext]',
                            outputPath: 'fonts/'
                        }
                    }
                ]
            },
            {
                test: /\.(jpg|png|gif|svg|pdf|ico)$/,
                use: [
                    {
                        loader: "file-loader",
                        options: {
                            name: '[hash:10].[ext]',
                            outputPath: 'images/'
                        }
                    }
                ]
            }
        ]
    }
});
