import Vue from 'vue'
import Vuex from 'vuex'

import user from './modules/user'
import train from './modules/train'

Vue.use(Vuex)


export default new Vuex.Store({
    modules: {
        user,
        train,
    }
})
