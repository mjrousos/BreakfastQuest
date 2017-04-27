// Transpiles/bundles/minifies client JS

const path = require('path');
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {

  entry: {
    main: path.resolve(__dirname, 'src/client/scripts/main')
  },

  target: 'web',

  output: {
    path: path.join(__dirname, './dist/public/scripts'),
    filename: '[name].packed.js'
  },

  module: {
    rules: [
      { test: /\.jsx?$/, exclude: /(node_modules)/, loader: 'babel-loader' },

      // css
      { test: /\.css$/, loader: ExtractTextPlugin.extract('css-loader?sourceMap') },

      // SVG images
      { test: /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/, loader: 'file-loader' }

      // Fonts
      // { test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/, loader: 'url-loader?limit=10000&minetype=application/font-woff' }
    ]
  },

  plugins: [
    // Generate an external css file
    new ExtractTextPlugin({
      filename: '[name].css',
      allChunks: true
    }),

    // Minify JS
    new webpack.optimize.UglifyJsPlugin({
     sourceMap: true
   })
  ],

  devtool: 'source-map'
};
