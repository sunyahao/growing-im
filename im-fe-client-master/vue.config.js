module.exports = {
  assetsDir: 'static',
  devServer: {
    open: true,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:7001'
      },
      '/public': {
        target: 'http://127.0.0.1:7001'
      },
      '/socket.io': {
        target: 'http://127.0.0.1:7002',
        ws: true
      }
    },
    port: 3001
  },
  configureWebpack: {
    plugins: [],
    externals: {
      BMap: 'BMap',
      vue: 'Vue',
      'vue-router': 'VueRouter',
      vuex: 'Vuex',
      vant: 'vant',
      xgplayer: 'Player',
      'socket.io-client': 'io',
      axios: 'axios',
      'vue-page-stack': 'VuePageStack',
      '@better-scroll/core': 'BScroll'
    }
  },
  css: {
    loaderOptions: {
      sass: {
        // 不再需要sass-resources-loader
        prependData: '@import "@/common/style/var/color.scss";@import "@/common/style/var/size.scss";'
      }
    }
  },
  lintOnSave : false
};
