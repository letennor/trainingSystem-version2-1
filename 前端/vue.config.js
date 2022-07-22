module.exports = {
  //关闭eslint
  lintOnSave: false,
  // 打包后的文件目录，打包后找不到css、js等文件问题
  publicPath: "./",

  devServer: {
    //代理跨域
    // proxy: {
    //   "/api": {
    //     target: "http://localhost:8000",
    //     // pathRewrite: { '^/api': '' },
    //     changeOrigin: true,
    //   },
    // },
    // 配置默认启动端口
    // port: 8081,
    // 配置项目启动时自动打开浏览器
    open: true,
  },
};
