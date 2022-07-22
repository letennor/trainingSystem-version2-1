import Vue from 'vue'
import App from './App.vue'

// 引入路由
import router from './router'
// 引入ElementUI
import ElementUI from 'element-ui'
// 引入ElementUI全部样式
import 'element-ui/lib/theme-chalk/index.css'
import store from '@/store/index'
// 引入vue-introjs
import VueIntro from 'vue-introjs'
import 'intro.js/introjs.css';
Vue.use(VueIntro)
Vue.config.productionTip = false

// 应用ElementUI
Vue.use(ElementUI)

new Vue({
  render: h => h(App),
  beforeCreate() {
    // 安装全局事件总线
    Vue.prototype.$bus = this;
  },
  // 注册路由
  router,
  store,
}).$mount('#app')
