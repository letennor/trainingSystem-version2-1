<template>
  <div>
    <el-container class="home-container" style="background-color: whitesmoke">
      <el-aside width="201px">
        <teacher-menu ref="teacherMenu" :key="timer" />
      </el-aside>

      <el-container>
        <el-header>
          <div><span>教师系统</span></div>
          <el-dropdown style="margin-left: 20px">
            <div class="el-dropdown-link">
              <div class="demo-basic--circle">
                <div class="block" style="display: flex; align-items: center">
                  <el-avatar
                    :size="40"
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
                  @click="$router.push('/TeacherHome/TeacherInfo')"
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
        </el-header>

        <el-main>
          <router-view name="mainView"></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
import TeacherMenu from "./TeacherMenu.vue";
import request from "@/api/request";
export default {
  data() {
    return {
      timer: "",
      isFold: false,
    };
  },
  components: {
    TeacherMenu,
  },
  methods: {
    fold() {
      this.$refs.teacherMenu.$data.isCollapse =
        !this.$refs.teacherMenu.$data.isCollapse;
      this.isFold = !this.isFold;
    },
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
    reload() {
      this.timer = new Date().getTime();
    },
    handleCommand(command) {
      if (command == "logout") {
        this.logout();
      }
    },
  },

  mounted() {
    //绑定刷新menu的事件
    console.log("绑定刷新menu的事件");
    this.$bus.$on("refreshMenu", (signal) => {
      console.log(signal);
      this.reload();
    });
  },
};
</script>

<style scoped>
/* .el-container {
  height: 100%;
} */
.home-container {
  height: 100vh;
}
.el-header /deep/ {
  display: flex;
  justify-content: space-between;
  background-color: #fff;
  color: #333;
  text-align: center;
  line-height: 60px;
  align-items: center;
  /* color: #fff; */
  font-size: 20px;
  border-bottom: 1px solid#e6e6e6;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  z-index: 10;
}

.el-menu {
  height: 100%;
}
.el-main {
  background-color: #f5f5f5;
}
.el-aside {
  background-color: #4e73df;
  border-right: none;
}
</style>
