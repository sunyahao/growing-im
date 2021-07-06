import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import Vant from 'vant';
import VuePageStack from 'vue-page-stack';
import AppInit from './app-init.js';
import '../public/static/jquery-3.6.0.js';

Vue.config.productionTip = false;
Vue.prototype.userId = "";
Vue.prototype.username = "";

Vue.use(Vant);
Vue.use(VuePageStack, { router });

AppInit().then(() => {
  new Vue({
    router,
    store,
    render: h => h(App)
  }).$mount('#app');
});
