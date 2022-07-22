// 配置路由的地方
import Vue from "vue";
import VueRouter from "vue-router";

// 使用路由组件
Vue.use(VueRouter);

// 引入路由组件
import Train from "@/pages/Train";
import homerouter from "../router/home";
import Practice from "@/pages/Train/containerMain/practice";
import Training from "@/pages/Train/containerMain/training";
import studyCharts from "@/pages/Train/containerMain/studyCharts";
import ModifyInfo from "@/pages/Train/containerMain/modifyInfo";
import Model1 from "@/pages/Train/trainModel/model1";
import Model2 from "@/pages/Train/trainModel/model2";
import Judge from "@/pages/Train/judge/judge";
import LevelTest from "@/pages/levelTest";
import ViewRecord from "@/pages/viewRecord";
import SetKnowledgePoints from "@/pages/Train/containerMain/setKnowledgePoints";
import ModelQuestion1 from "@/pages/Train/trainModel/modelQuestion1";
import judge1 from "@/pages/Train/judge/judge1";
import learnKnowledge from "@/pages/Train/containerMain/learnKnowledge";
import Learning from "@/pages/Train/Learning";
import message from "@/pages/Train/containerMain/message";
import comment from "@/pages/Train/comment/comment";
import notice from '@/pages/Train/containerMain/notice'
// 引入管理员所用路由组件
import adminRouter from "./admin";
// 引入教师所用路由组件
import teacherRouter from "./teacher";

//先把VueRouter原型对象的push、replace保存一份
let originPush = VueRouter.prototype.push;
let originReplace = VueRouter.prototype.replace;

//重写push、replace方法
//第一个参数：告诉原来push方法，你往哪里跳转（传递哪些参数）
//第二个参数：成功回调
//第三个参数：失败的回调
//call||apply区别
//相同点：都可以调用函数一次，都可以篡改函数的上下文一次
//不同点：call与apply传递参数：call传递参数用逗号隔开，apply方法传递数组
VueRouter.prototype.push = function (location, resolve, reject) {
  if (resolve && reject) {
    originPush.call(this, location, resolve, reject);
  } else {
    originPush.call(
      this,
      location,
      () => {},
      () => {}
    );
  }
};
VueRouter.prototype.replace = function (location, resolve, reject) {
  if (resolve && reject) {
    originReplace.call(this, location, resolve, reject);
  } else {
    originReplace.call(
      this,
      location,
      () => {},
      () => {}
    );
  }
};

//配置路由
const router = new VueRouter({
  //配置路由、
  routes: [
    {
      path: "/trainHome",
      component: Train,
      redirect: "/trainHome/training",
      children: [
        {
          path: "/trainHome/practiceRecord",
          component: Practice,
        },
        {
            name:'Training',
          path: "/trainHome/training",
          component: Training,
        },
        {
          path: "/trainHome/studyCharts",
          component: studyCharts,
        },
        {
          path: "/trainHome/modifyInfo",
          component: ModifyInfo,
        },
        {
          path: "/trainHome/setKnowledgePoints",
          component: SetKnowledgePoints,
        },
        {
          path: "/trainHome/learnKnowledge",
          component: learnKnowledge,
        },
        {
          path: "/trainHome/message",
          component: message,
        },
        {
          path: "/trainHome/notice",
          component: notice
        },
      ],
    },
    // 配置管理员路由组件
    ...adminRouter,
    // 配置教师路由组件
    ...teacherRouter,
    ...homerouter,
    {
      path: "/trainModel1",
      component: Model1,
      // redirect: '/question1',
    },
    {
      path: "/modelQuestion1",
      component: ModelQuestion1,
    },
    {
      path: "/judge1",
      component: judge1,
    },
    {
      path: "/trainModel2",
      component: Model2,
    },
    {
      path: "/judge",
      component: Judge,
    },
    {
      path: "/levelTest",
      component: LevelTest,
    },
    {
      path: "/viewRecord",
      component: ViewRecord,
    },
    {
      path: "/learning",
      component: Learning,
    },
    {
      path: "/comment",
      component: comment,
    },

    //重定向，在路由跑起来的时候，立马让他重定向到首页
    // {
    //     path: '*',
    //     redirect: "/home"
    // }
  ],
});

router.beforeEach((to, from, next) => {
  if (to.path === "/login" || to.path === "/" || to.path === "/register")
    return next();
  const tokenStr = window.sessionStorage.getItem("token");
  if (!tokenStr) return next("/login");
  if (
    to.path != "/judge" &&
    to.path != "/trainModel2" &&
    to.path != "/judge1" &&
    to.path != "/modelQuestion1"
  ) {
    let sessionKeys = Object.keys(sessionStorage);
    for (let i = 0; i < sessionKeys.length; i++) {
      if (
        sessionKeys[i] != "token" &&
        sessionKeys[i] != "account" &&
        sessionKeys[i] != "level" &&
        sessionKeys[i] != "name" &&
        sessionKeys[i] != "userType"
      ) {
        window.sessionStorage.removeItem(sessionKeys[i]);
      }
    }
  }
  next();
});

export default router;
