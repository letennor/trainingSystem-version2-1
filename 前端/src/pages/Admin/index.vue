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
          管理员系统
        </h5>
        <el-divider class="aside-divider"></el-divider>
      </div>
      <div class="toggle-button" @click="toggleCollapse">
        <i :class="isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'"></i>
      </div>
      <el-menu
        :default-active="$route.path"
        class="el-menu-vertical-demo"
        text-color="#cddaf0"
        active-text-color="#fff"
        background-color="#4e73df"
        router
        :collapse="isCollapse"
        :collapse-transition="false"
      >
        <el-menu-item index="/adminHome/systemInfo">
          <i class="el-icon-data-analysis"></i>
          <span>系统概况</span>
          <el-divider class="aside-divider"></el-divider>
        </el-menu-item>
        <el-submenu index="handleApplication">
          <template slot="title">
            <i class="el-icon-s-check"></i>
            <span>处理申请</span>
            <el-badge
              :value="allAskCount"
              class="item"
              :type="`${allAskCount == 0 ? 'success' : 'danger'}`"
              :is-dot="isCollapse"
              v-if="allAskCount != 0"
            />
          </template>

          <el-menu-item index="/adminHome/handleStuApplication">
            <i class="el-icon-chat-line-square"></i>学生申请<el-badge
              :value="stuAskCount"
              class="item"
              :type="`${stuAskCount == 0 ? 'success' : 'danger'}`"
              v-if="stuAskCount != 0"
            />
            <el-divider class="aside-divider"></el-divider>
          </el-menu-item>
          <el-menu-item index="/adminHome/handleTeaApplication">
            <i class="el-icon-chat-line-square"></i>老师申请<el-badge
              :value="teaAskCount"
              class="item"
              :type="`${teaAskCount == 0 ? 'success' : 'danger'}`"
              v-if="teaAskCount != 0"
            />
          </el-menu-item>
        </el-submenu>
        <el-submenu index="accountManage">
          <template slot="title">
            <el-divider class="aside-divider"></el-divider>
            <i class="el-icon-menu"></i>
            <span>账号管理</span>
          </template>
          <el-menu-item index="/adminHome/addTeacher">
            <i class="el-icon-circle-plus-outline"></i>添加老师账号
            <el-divider class="aside-divider"></el-divider>
          </el-menu-item>
          <el-menu-item index="/adminHome/handleAccount"
            ><i class="el-icon-setting"></i>管理用户账号</el-menu-item
          >
        </el-submenu>
        <!-- <el-menu-item index="/adminHome/systemMaintain">
            <i class="el-icon-document"></i>
            <span slot="title">系统维护</span>
          </el-menu-item> -->
      </el-menu>
    </el-aside>

    <el-container>
      <el-header>
        <div><span></span></div>
        <el-dropdown style="margin-left: 20px">
          <div class="el-dropdown-link">
            <div class="demo-basic--circle">
              <div class="block" style="display: flex; align-items: center">
                <el-avatar
                  :size="40"
                  src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
                >
                </el-avatar>
                <span><i class="el-icon-arrow-down el-icon--right"></i> </span>
              </div>
            </div>
          </div>
          <el-dropdown-menu slot="dropdown">
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
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import request from "@/api/request";
export default {
  name: "Admin",
  data() {
    return {
      stuAskCount: 0,
      teaAskCount: 0,
      isCollapse: false,
    };
  },
  computed: {
    allAskCount() {
      return this.stuAskCount + this.teaAskCount;
    },
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
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
    },
  },
  created() {
    request({
      url: "/admin/studentAsk",
      method: "POST",
      data: { adminId: window.sessionStorage.getItem("token") },
    }).then((res) => {
      if (res.code == 200) {
        res.data.forEach((ask) => {
          if (ask.isCheck == 0) {
            this.stuAskCount++;
          }
        });
      }
    });
    request({
      url: "/admin/teacherAsk",
      method: "POST",
      data: { adminId: window.sessionStorage.getItem("token") },
    }).then((res) => {
      if (res.code == 200) {
        res.data.forEach((ask) => {
          if (ask.isCheck == 0) {
            this.teaAskCount++;
          }
        });
      }
    });
  },
  mounted() {
    this.$bus.$on("countSub", (type) => {
      if (type == "stuAskCount") {
        this.stuAskCount--;
      } else if (type == "teaAskCount") {
        this.teaAskCount--;
      }
    });
  },
};
</script>

<style scoped>
.home-container {
  height: 100vh;
}
.el-header {
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

.el-aside {
  display: block;
  background-color: #4e73df;
  color: #333;
  /* line-height: 200px; */
  border-right: 1px solid#e6e6e6;
  transition: width 0.3s;
}
.el-aside .el-menu {
  /* 清除导航栏默认的边框 */
  border-right: none;
}

.el-main {
  position: relative;
  background-color: #fff;
  color: #333;
  background-color: #f5f5f5;
  /* text-align: center; */
  /* line-height: 160px; */
}

.item {
  /* padding-left: 10px; */
  margin-top: -10px;
}
.toggle-button {
  height: 40px;
  line-height: 40px;
  font-size: 25px;
  text-align: center;
  color: rgb(255, 255, 255);
  cursor: pointer;
}

.aside-divider {
  margin: 0px 0px;
  background-color: #829be7;
  z-index: 10;
  /* background-color: #bf1010; */
  /* width: 80%;
  padding: 0px 10px; */
}
.el-menu-item i,
.el-submenu i {
  color: #cddaf0;
}
.el-menu .is-active {
  font-weight: 700;
}
.el-menu-item:hover {
  background-color: rgb(69, 66, 238) !important;
}
.el-menu-item:focus {
  background-color: rgb(69, 66, 238);
}
</style>
