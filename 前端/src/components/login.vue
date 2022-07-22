<template>
  <div class="all">
    <div>
      <div style="height: 50px"></div>
      <div class="row">
        <div class="col-lg-2"></div>
        <div class="col-lg-4">
          <img src="../assets/images/home1.png"
               alt=""
               style="width: 500px; height: 488px" />
        </div>
        <div class="col-lg-4">
          <el-card class="box-card">
            <div class="header">登录</div>
            <div class="form-wrapper">
              <el-form :model="ruleForm"
                       :rules="rules"
                       ref="ruleForm"
                       class="demo-ruleForm">
                <el-form-item prop="name">
                  <el-input v-model="ruleForm.name"
                            placeholder="用户名"
                            class="input-item"></el-input>
                </el-form-item>
                <el-form-item prop="pass">
                  <el-input type="password"
                            v-model="ruleForm.pass"
                            placeholder="密码"
                            class="input-item"
                            show-password></el-input>
                </el-form-item>
                <el-form-item prop="userType"
                              style="margin-top: 10px; padding-top: 10px">
                  <el-radio-group v-model="ruleForm.userType">
                    <el-radio-button label="1">学生</el-radio-button>
                    <el-radio-button label="2">教师</el-radio-button>
                    <el-radio-button label="3">管理员</el-radio-button>
                  </el-radio-group>
                </el-form-item>
                <div class="btn"
                     v-on:click="submitForm('ruleForm')">登录</div>
              </el-form>
            </div>
            <el-alert v-if="setAlert==true"
                      title="用户名或密码错误"
                      type="error"
                      center
                      show-icon
                      @close="closeAlert">
            </el-alert>

            <div class="msg">
              还没有账号？
              <a>
                <router-link to="/register">点击注册</router-link>
              </a>
            </div>
          </el-card>
        </div>
      </div>
      <div style="height: 50px"></div>
    </div>
  </div>
</template>

<script>
import request from '../api/request'
export default {
  name: 'login',
  data() {
    var validatename = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入用户名'))
      }
      callback()
    }
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      }
      callback()
    }

    return {
      ruleForm: {
        name: '',
        pass: '',
        userType: '',
      },
      rules: {
        pass: [{ validator: validatePass, trigger: 'blur' }],
        name: [{ validator: validatename, trigger: 'blur' }],
        userType: [
          { required: true, message: '请选择账号类型', trigger: 'change' },
        ],
      },
      setAlert: false, // 控制登录错误时的警告框
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 点击登录时将登录错误提示关闭
          this.setAlert = false
          request({
            url: '/login',
            method: 'post',
            data: {
              account: this.ruleForm.name,
              pwd: this.ruleForm.pass,
              userType: this.ruleForm.userType,
            },
          }).then((res) => {
            console.log(res)
            if (res.code == 200) {
              if (this.ruleForm.userType == 1) {
                window.sessionStorage.setItem('token', res.data.userId)
                window.sessionStorage.setItem('level', res.data.userLevel)
                window.sessionStorage.setItem('account', res.data.account)
                window.sessionStorage.setItem('name', res.data.name)
                window.sessionStorage.setItem(
                  'userType',
                  this.ruleForm.userType
                )
                request({
                  url: '/inputRedis',
                  method: 'POST',
                  data: {
                    userId: res.data.userId,
                  },
                })
                this.$router.push('/trainHome')
              } else if (this.ruleForm.userType == 3) {
                window.sessionStorage.setItem('token', res.data.userId)
                window.sessionStorage.setItem(
                  'userType',
                  this.ruleForm.userType
                )
                window.sessionStorage.setItem('name', res.data.name)
                request({
                  url: '/inputRedis',
                  method: 'POST',
                  data: {
                    userId: res.data.userId,
                  },
                })
                this.$router.replace('/adminHome')
              } else if (this.ruleForm.userType == 2) {
                window.sessionStorage.setItem('token', res.data.userId)
                window.sessionStorage.setItem(
                  'userType',
                  this.ruleForm.userType
                )
                window.sessionStorage.setItem('name', res.data.name)
                request({
                  url: '/inputRedis',
                  method: 'POST',
                  data: {
                    userId: res.data.userId,
                  },
                })
                this.$router.replace('/TeacherHome')
              }
            } else {
              // 登录错误时开启提示
              this.setAlert = true
            }
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    // 关闭警告时的回调
    closeAlert() {
      this.setAlert = false
    },
  },
}
</script>

<style scoped>
@import '../assets/css/bootstrap.min.css';
.all {
  background-image: linear-gradient(
    to right,
    #d2f3d7,
    rgba(186, 228, 229, 0.58)
  );
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  position: fixed;
  background-size: 100% 100%;
}
.box-card {
  position: relative;
  background-color: #fff;
  width: 370px;
  height: 488px;
  border-radius: 1px;
  padding: 0 50px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.header {
  font-size: 38px;
  font-weight: bold;
  text-align: center;
  line-height: 100px;
}
.input-item {
  display: block;
  width: 100%;
  /* margin-bottom: 10px; */
  border: 0;
  padding: 5px;
  border-bottom: 1px solid rgb(128, 125, 125);
  font-size: 15px;
}
.input-item /deep/ .el-input__inner {
  border: 0;
}
/* .input-item placeholder {
  text-transform: uppercase;
} */
.btn {
  text-align: center;
  padding: 10px;
  width: 100%;
  margin-top: 10px;
  background-image: linear-gradient(to right, #1668c7, #1668c7);
  color: #fff;
}
.msg {
  position: absolute;
  bottom: 10px;
  /* text-align: center; */
  line-height: 44px;
  padding: 0 33px;
}
a {
  text-decoration-line: none;
  color: #4a72c2;
}
.el-alert {
  margin-top: 10px;
}
.el-form-item {
  margin-bottom: 17px;
}
</style>
