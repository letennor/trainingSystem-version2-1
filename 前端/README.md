# 路由跳转问题（待解决）：
replace 与 push 问题
1. 打开做题页面，如果退出后则不能通过返回进入之前的做题界面
2. 提交判题结果后，不能通过返回进入之前的页面
3. 做题和判题中，点击题号的路由跳转问题？？？
。。。等

# 项目目录 （粗略）
- src
    - api ：对axios的封装和配置
        - index.js ：暴露封装好的request对象
        - request.js ：对axios二次封装。创建axios对象，设置请求和响应拦截器。
    - assets ：静态文件，所依赖的css、js、images、fonts
    - components ：组件
        - login.vue ：登录页面组件
        - register.vue ：注册页面组件
        - webhome.vue ：首页组件
        - middle.vue ：首页中用到的子组件
        - homefoot.vue ： 首页中用到的子组件
    - pages ：路由组件
        - Train ：训练系统相关组件
            - containerMain ：训练系统主界面相关组件
                - info.vue ：展示个人信息页面组件
                - modifyInfo.vue ：修改个人信息页面组件
                - practice.vue ：查看练习记录页面组件
                - setKnowledgePoints.vue ：模式三勾选知识点页面组件
                - studyCharts.vue ：练习记录统计图表页面组件
                - training.vue ：训练模式页面组件
                - welcome.vue ：欢迎页面组件
            - judge 批改答案页面组件（模式一、二、三）
                - judge.vue
            - trainModel ：进入训练后相关组件
                - model1.vue 模式一学习知识点页面组件
                - model2.vue ：做题页面组件（包括模式一、二、三）
            - index.vue ：训练系统页面结构组件
            - question_1.vue ：暂未用到，之前思路代码遗留
        - levelTest.vue ：水平测试页面组件
        - viewRecord.vue ：查看单次练习记录页面组件
        - router ：路由配置信息
            - home.js ：首页所用路由配置信息（首页、登录、注册）
            - index.js ：其余路由配置信息
            - person.js ：空
        - store ：vuex配置信息
            - modules ：模块化配置vuex，现在未用到（用session替代），之前思路代码遗留
                - train.js ：训练相关
                - user.js ：个人信息相关
            - index.js ：vuex配置文件
        - App.vue ：App组件
        - main.js ：项目入口文件
- vue.config.js ：项目配置文件



# dachuang_0

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
