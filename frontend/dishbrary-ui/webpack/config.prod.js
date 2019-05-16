
const merge = require("webpack-merge");
const baseConfig = require("./config.base.js");

module.exports = merge(baseConfig, {
    mode: "production",
    module: {
        rules: [
            {
                test: /\.html$/,
                use: "html-loader"
            },
            {
                test: /\.css$/,
                use: ["style-loader", "css-loader?modules"]
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
