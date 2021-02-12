/*
 * @Description: 配置文件
 * @Author: Aaron.Shen
 * @Date: 2020-02-07 16:23:00
 * @LastEditors: Aaron.Shen
 * @LastEditTime: 2020-03-05 01:41:38
 */

const port = process.env.port || process.env.npm_config_port || 8080; // 端口

module.exports = {
  //publicPath: "./",
  publicPath: process.env.NODE_ENV === "production" ? "/" : "/",
  // 在npm run build 或 yarn build 时 ，生成文件的目录名称（要和baseUrl的生产环境路径一致）（默认dist）
  outputDir: "dist",
  // 用于放置生成的静态资源 (js、css、img、fonts) 的；（项目打包之后，静态资源会放在这个文件夹下）
  assetsDir: "static",
  // 是否开启eslint保存检测，有效值：ture | false | 'error'
  lintOnSave: process.env.NODE_ENV === "development",
  // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
  productionSourceMap: false,
  devServer: {
    host: "0.0.0.0",
    port: port,
    open: true,
    proxy: {
      // detail: https://cli.vuejs.org/config/#devserver-proxy
      [process.env.VUE_APP_BASE_API]: {
        target: `http://47.98.128.88:8086/api/`,
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_BASE_API]: "",
        },
      },
      /* "/api": {
        target: 'http://localhost:8086/', // 本地后端地址
        //target: "http://47.115.85.237:3000/", // 线上后端地址
        changeOrigin: true, //允许跨域
        pathRewrite: {
          "^/api": "",
        },
      }, */
    },
  },
};
