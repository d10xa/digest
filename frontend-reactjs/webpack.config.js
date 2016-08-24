module.exports = {
    entry: './src/index.jsx',
    output: {
        path: './build',
        filename: 'bundle.js'
    },
    module: {
        loaders: [
            {
                test: /\.jsx$/,
                loader: 'babel-loader',
                query: {
                    presets: ['es2015', 'react']
                }
            }
        ]
    },
    resolve: {
        extensions: ['', '.js', '.jsx']
    }
};
