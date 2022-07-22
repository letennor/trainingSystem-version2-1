<template>
  <div class="all">
    <div style="height: 50px">

    </div>
    <div class="row">
      <div class="col-lg-1">

      </div>
      <div class="col-lg-4">
        <img src="../assets/images/home2.png"
             alt=""
             style="width: 480px;height: 480px;">
      </div>
      <div class="col-lg-4">
        <el-card class="box-card">
          <div slot="header"
               style="text-align: center;font-size: 24px;">
            <span>注册</span>
          </div>

          <el-form :model="student"
                   :rules="rules"
                   ref="student">
            <el-row :gutter="30">
              <el-col :span="11">
                <el-form-item prop="name">
                  <el-input prefix-icon="el-icon-user-solid"
                            placeholder="请输入姓名"
                            v-model="student.name"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="13">
                <el-form-item prop="college">
                  <el-input prefix-icon="el-icon-s-home"
                            placeholder="请输入学校"
                            v-model="student.college"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item prop="major">
              <el-input prefix-icon="el-icon-reading"
                        placeholder="请输入专业"
                        v-model="student.major"></el-input>
            </el-form-item>
            <el-row :gutter="30">
              <el-col :span="12">
                <el-form-item prop="account">
                  <el-input prefix-icon="el-icon-s-custom"
                            placeholder="请输入学号"
                            v-model="student.account"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item prop="pwd">
                  <el-input type="password"
                            prefix-icon="el-icon-lock"
                            placeholder="请输入密码"
                            v-model="student.pwd"
                            show-password></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item prop="email">
              <el-input prefix-icon="el-icon-s-promotion"
                        placeholder="请输入邮箱"
                        v-model="student.email"></el-input>
            </el-form-item>
          </el-form>
          <div>

          </div>
          <el-button class="btn"
                     v-on:click.once="submitForm('student')">注册</el-button>
          <div class="msg">
            已有账号
            <a>
              <router-link to="/login">去登录</router-link>
            </a>
          </div>
        </el-card>
      </div>
    </div>
    <div style="height: 50px">

    </div>
  </div>
</template>

<script>
import request from '../api/request'
export default {
  name: 'register',
  data() {
    // var checkEmail = (rule,value,cb) =>{
    //   const regEmail =  /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
    //   if(regEmail.test(value)){
    //     //合法的邮箱
    //     return cb()
    //   }
    //   cb(new Error("请输入合法的邮箱"))
    // }
    let checkAccount = (rule, value, cb) => {
      const regAccount = /^[1-9]\d*$/
      if (regAccount.test(value)) {
        return cb()
      }
      cb(new Error('学号必须为纯数字且不能以零开头'))
    }
    return {
      student: {
        name: '',
        college: '',
        major: '',
        account: '',
        pwd: '',
        email: '',
      },
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        college: [{ required: true, message: '请输入学校', trigger: 'blur' }],
        major: [{ required: true, message: '请输入专业', trigger: 'blur' }],
        account: [
          { required: true, message: '请输入学号', trigger: 'blur' },
          { validator: checkAccount, trigger: 'blur' },
          { min: 6, max: 10, message: '学号必须为6~10位', trigger: 'blur' },
        ],
        pwd: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 8, max: 16, message: '密码必须为8~16位', trigger: 'blur' },
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱', trigger: 'blur' },
        ],
      },
    }
  },
  methods: {
    submitForm(student) {
      this.$refs[student].validate((valid) => {
        if (valid) {
          request({
            url: '/register/basicInfo',
            method: 'post',
            data: this.student,
          }).then((res) => {
            if (res.code == 200) {
              alert('注册成功')
              this.$router.push('/login')
            } else {
              alert(res.msg)
            }
          })
        }
      })
    },
  },
}
</script>

<style scoped>
@import '../assets/css/bootstrap.min.css';
.box-card {
  width: 580px;
  height: 480px;
  border-radius: 2px;
}
.all {
  background-image: linear-gradient(
    to right,
    #d2f3d7,
    rgba(186, 228, 229, 0.58)
  );
  min-width: 100%;
  min-height: 100%;
  position: relative;
  background-size: 100% auto;
}
.input {
  border-radius: 4px;
  border: 5px;
  box-shadow: 0 2px 12px 0 rgba(163, 60, 60, 0.1);
}
.btn {
  text-align: center;
  padding: 10px;
  width: 100%;
  margin-top: 10px;
  background-image: linear-gradient(to right, #1668c7, #1668c7);
  color: #fff;
}

.msg {
  text-align: center;
  line-height: 50px;
}
a {
  text-decoration-line: none;
  color: #4a72c2;
}
</style>
