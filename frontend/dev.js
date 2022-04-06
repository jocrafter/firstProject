const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
  module: {
      rules: [
        {
          test: /\.css$/i,
          use: [
           { loader: "style-loader", options: { injectType: "styleTag" } },
            "css-loader",
            ],
        },
      ],
    },
  devServer: {
    port: 9000,
    watchFiles: {
      paths: ['src/**'],
      options: {
        usePolling: false,
      },

    },
      proxy: [{
          context: ['/api/**'],
          target: "http://localhost:8080/",
          changeOrigin: true,
      }]
  },
  mode: 'development',
  plugins: [
    new HtmlWebpackPlugin({
            template: './src/index.html',
            filename:'./index.html'
        })
  ]
};