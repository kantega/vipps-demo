var path = require('path');

module.exports = {
    entry: './js/index.js',
    devtool: 'sourcemaps',
    cache: true,
    mode: 'development',
    output: {
        path: __dirname,
        filename: '../resources/static/built/bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"],
                        plugins: [
                            '@babel/transform-runtime'
                        ]                    }
                }]
            }
        ]
    },
    devServer: {
        contentBase: path.join(__dirname, '../resources/templates'),
        port: 3000,
        publicPath: '/',
        hot: true,
    }
};
