<template>
  <div>
    <header class="header-area header-sticky">

      <div class="row">
        <div class="col-12">
          <nav class="main-nav">
            <!-- ***** Logo Start ***** -->
            <div class="logo">
              <i class="el-icon-s-home"></i>
              大学生竞赛个性化训练系统
            </div>

            <!-- ***** Logo End ***** -->
            <!-- ***** Menu Start ***** <router-link :to="`${userType == 1 ? '/trainHome' : userType == 2 ? 'TeacherHome' : '/adminHome'}`">进入系统<i class="el-icon-s-custom"></i></router-link>-->
            <ul class="nav">
              <li class="scroll-to-section"
                  v-show="isLogin">
                <el-button type="text"
                           @click="returnSystem"
                           style="font-size: 17px; color: #ffffff">返回系统<i class="el-icon-s-custom"></i></el-button>
              </li>
              <li class="scroll-to-section"
                  v-show="!isLogin">
                <router-link to="/register">注册 <i class="el-icon-edit-outline"></i></router-link>
              </li>
              <li class="scroll-to-section"
                  v-show="!isLogin">
                <router-link to="/login">登录<i class="el-icon-s-custom"></i></router-link>
              </li>
            </ul>
            <!-- ***** Menu End ***** -->
          </nav>
        </div>
      </div>
    </header>
    <section class="main-banner"
             id="top">

      <div class="row">

        <div class="col-lg-6 ">

          <div class="right-image">
            <img src="../assets/images/main.png"
                 alt="" />
          </div>

        </div>
        <div class="col-lg-6">

          <div class="header-text">
            <h2>为你定制 <br><em>你的专属训练计划</em></h2>
            <!--            <div class="main-button-gradient">-->
            <!--              <div class="scroll-to-section">-->
            <!--                <router-link to="/login">   To Login!</router-link>-->
            <!--              </div>-->
            <!--            </div>-->
          </div>
        </div>
      </div>
    </section>
    <middle></middle>
    <foot></foot>
  </div>
</template>

<script>
export default {
  name: 'webhome',
  computed: {
    isLogin() {
      if (window.sessionStorage.getItem('token')) {
        return true
      } else {
        return false
      }
    },
    userType() {
      let userType = window.sessionStorage.getItem('userType')
      if (userType) {
        return userType
      }
      return null
    },
  },
  methods: {
    returnSystem() {
      if (this.userType) {
        if (this.userType == 1) {
          console.log('1')
          this.$router.push('/trainHome')
        } else if (this.userType == 2) {
          this.$router.push('TeacherHome')
        } else if (this.userType == 3) {
          this.$router.push('/adminHome')
        }
      } else {
        window.sessionStorage.clear()
        this.$message({
          message: '身份验证已过期，请重新登录',
          type: 'warning',
        })
        this.$router.push('/login')
      }
    },
  },
  components: {
    middle: () => import('../components/middle'),
    foot: () => import('../components/homefoot'),
  },
}
</script>

<style scoped>
@import '../assets/css/templatemo-eduwell-style.css';
@import '../assets/css/bootstrap.min.css';
.nav {
  margin-right: 15px !important;
}
li a {
  font-size: 17px !important;
}
</style>
