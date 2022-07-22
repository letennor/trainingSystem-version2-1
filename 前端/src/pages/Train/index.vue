<template>
  <el-container class="home-container">
    <el-aside :width="isCollapse ? '65px' : '201px'">
      <div style="padding: 10px 20px">
        <h5
          style="
            text-align: center;
            margin-top: 10px;
            color: #fff;
            margin-bottom: 20px;
          "
        >
          竞赛训练系统
        </h5>
        <el-divider class="aside-divider" style="margin-top: 20px !important;"></el-divider>
      </div>
      <div class="toggle-button" @click="toggleCollapse">
        <i :class="isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'"></i>
      </div>
      <el-menu
        :default-active="$route.path"
        class="el-menu-vertical-demo"
        text-color="#cddaf0"
        active-text-color="#fff"
        unique-opened
        :collapse="isCollapse"
        :collapse-transition="false"
        router
      >
        <!--          <el-menu-item index="/trainHome/info">-->
        <!--            <i class="el-icon-user-solid"></i>-->
        <!--            <span slot="title">个人信息</span>-->
        <!--          </el-menu-item>-->
        <el-menu-item index="/trainHome/training" class="step1">
          <i class="el-icon-edit"></i>
          <!-- <span slot="title">开始训练</span> -->
          <span>开始训练</span>
          <el-divider class="aside-divider"></el-divider>
        </el-menu-item>
        <!-- <el-divider class="aside-divider"></el-divider> -->
        <!-- <el-menu-item index="/trainHome/learnKnowledge">
            <i class="el-icon-reading"></i>
            <span slot="title">学习知识点</span>
          </el-menu-item> -->
        <el-menu-item index="/trainHome/message" class="step2">
          <i class="el-icon-message"></i>
          <!-- <span slot="title">留言管理</span> -->
          <span>留言管理</span>
          <el-divider class="aside-divider"></el-divider>
        </el-menu-item>
        <!-- <el-divider class="aside-divider"></el-divider> -->
        <el-menu-item index="/trainHome/studyCharts" class="step3">
          <i class="el-icon-s-marketing"></i>
          <!-- <span slot="title">学习统计</span> -->
          <span>学习统计</span>
          <el-divider class="aside-divider"></el-divider>
        </el-menu-item>
        <!-- <el-divider class="aside-divider"></el-divider> -->
        <el-menu-item index="/trainHome/practiceRecord" class="step4">
          <i class="el-icon-reading"></i>
          <span slot="title">练习记录</span>
        </el-menu-item>
      </el-menu>
      <!-- <div style="text-align: center; margin-top: 20px">
        <el-button
          class="menu-collapse"
          type="primary"
          icon="el-icon-caret-left"
          size="small"
          circle
        ></el-button>
      </div> -->
    </el-aside>

    <el-container>
      <el-header>
        <div>
          <h3></h3>
        </div>
        <div class="header-right">
          <el-link
            :underline="false"
            style="margin-right: 20px"
            icon="el-icon-s-home"
            @click="$router.push('/')"
            >首页</el-link
          >
          <el-divider direction="vertical"></el-divider>
          <el-link
            :underline="false"
            style="margin-right: 20px; margin-left: 20px"
            icon="el-icon-chat-dot-square"
            @click="$router.push('/trainHome/notice')"
            class="step5"
            >我的消息
            <el-badge
              :value="unreadNotifyCount"
              class="item"
              type="danger"
              :is-dot="true"
              v-if="unreadNotifyCount != 0"
            />
          </el-link>

          <el-divider direction="vertical"></el-divider>
          <el-dropdown style="margin-left: 20px">
            <div class="el-dropdown-link">
              <div class="demo-basic--circle">
                <div class="block" style="display: flex; align-items: center">
                  <el-avatar
                    :size="40"
                    class="step6"
                    src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
                  >
                  </el-avatar>
                  <span
                    ><i class="el-icon-arrow-down el-icon--right"></i>
                  </span>
                </div>
              </div>
            </div>

            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-link
                  :underline="false"
                  icon="el-icon-user"
                  @click="$router.push('/trainHome/modifyInfo')"
                  >个人中心</el-link
                >
              </el-dropdown-item>
              <el-dropdown-item>
                <el-link
                  :underline="false"
                  icon="el-icon-switch-button"
                  @click="logout"
                  >退出</el-link
                >
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import request from "@/api/request";
export default {
  name: "Train",
  data() {
    return {
      isCollapse: false,
      name: sessionStorage.getItem("name"),
      unreadNotifyCount: 0, // 未读消息数
    };
  },
  methods: {
    logout() {
      request({
        url: "/logout",
        method: "POST",
        data: {
          userId: window.sessionStorage.getItem("token"),
        },
      });
      window.sessionStorage.clear();
      this.$router.push("/");
    },
    // 点击按钮切换菜单的折叠与展开
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
    },
    getNotify() {
      request({
        url: "/notification/received",
        method: "POST",
        data: {
          userId: sessionStorage.getItem("token"),
        },
      }).then((res) => {
        this.unreadNotifyCount = 0;
        res.data.forEach((notify) => {
          if (notify.isCheck == 0) {
            this.unreadNotifyCount++;
          }
        });
      });
    },
    //设置新手引导
    setGuide() {
      // 通过id或者class绑定步骤，也可以直接定死在div上面
      let steps = [
        {
          element: ".step1",
          intro: "步骤1：在这里，您可以进行我们为您专门设计的个性化训练",
          position: "left",
        },
        {
          element: ".step2",
          intro: "步骤2：在这里，您可以给我们的老师留言，解答学习过程中的疑惑",
          position: "left",
        },

        {
          element: ".step3",
          intro: "步骤3：在这里，您可以看到您所有的训练的统计数据",
          position: "left",
        },

        {
          element: ".step4",
          intro: "步骤4：在这里，您可以查看已经做过的试题",
          position: "left",
        },

        {
          element: ".step5",
          intro: "步骤5：在这里，您可以看到系统的消息",
          position: "left",
        },

        {
          element: ".step6",
          intro: "步骤6：这里是个人中心，您可以在这里进入个人中心修改个人信息",
          position: "left",
        },

        {
          element: ".step7",
          intro: "现在，让我们启程吧！😀",
          position: "center",
        },
      ];

      //引入intro.js
      var introJS = require("intro.js");
      introJS()
        .setOptions({
          // 我给看的效果图是英文的（默认英文），这里是更换成中文
          prevLabel: "上一步",
          nextLabel: "下一步",
          skipLabel: "跳过",
          doneLabel: "完成",
          // steps步骤，可以写个工具类保存起来
          steps: steps,
        })
        .oncomplete(() => {
          // 需要注意的是，这里用了箭头函数
          // 点击结束按钮后执行的事件
        })
        .onexit(() => {
          // 点击跳过按钮后执行的事件
        })
        .onbeforeexit(() => {
          // 确认完毕之后执行的事件
          // return confirm('Are you sure?')
        })
        .start();
    },
  },
  created() {
    this.getNotify();
  },
  mounted() {
    this.$bus.$on("countSub", () => {
      this.unreadNotifyCount--;
    });

    //绑定自定义事件
    // this.$bus.$on('guild', () => {
    //   setTimeout(() => {
    //     this.setGuide()
    //   }, 200)
    // })

    //如果这个人是刚进来的，即level=0，则触发新手引导
    if (sessionStorage.getItem("level") == 0) {
      this.setGuide();
    }
  },
};
</script>

<style scoped>
.home-container {
  height: 100vh;
}
.el-header {
  background-color: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: rgb(0, 0, 0);
  font-size: 20px;
  border-bottom: 1px solid#e6e6e6;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  z-index: 10;
}
.el-header .div {
  display: flex;
  align-items: center;
}
.header-right {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-aside {
  background-color: #4e73df;
  display: block;
  border-right: 1px solid#e6e6e6;
  transition: width 0.3s;
}
.el-aside .el-menu {
  border-right: none;
  background-color: #4e73df;
}

.el-main {
  background-color: #f5f5f5;
}
.toggle-button {
  font-size: 25px;
  text-align: center;
  /* color: rgb(100, 97, 97); */
  color: #fff;
  cursor: pointer;
}

.aside-divider {
  margin: 0px 0px;
  background-color: #829be7;
  /* width: 80%;
  padding: 0px 10px; */
}
.el-menu-item i {
  color: #cddaf0;
}
.el-menu .is-active {
  font-weight: 700;
}
.el-menu-item:hover {
  background-color: rgb(69, 66, 238);
}
.el-menu-item:focus {
  background-color: rgb(69, 66, 238);
}
.menu-collapse {
  background-color: rgba(255,255,255, 0.3);
}
</style>
