<template>
  <div  class="infos">
    <el-row>

      <el-col :span="12">
        <el-dropdown @command="handleCommand">
          <el-button type="primary"
                     style="width: 200px">
            {{userLevel}}<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>

          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="1">模式1</el-dropdown-item>
            <el-dropdown-item command="2">模式2</el-dropdown-item>
            <el-dropdown-item command="3">模式3</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

      </el-col>

      <el-col :span="12">
        <el-input placeholder="输入学生学号"
                  v-model="queryAccount"
                  class="input-with-select">

          <el-button slot="append"
                     icon="el-icon-search"
                     @click="queryStudentByAccount"></el-button>
        </el-input>
      </el-col>

    </el-row>

    <el-row style="margin-top: 20px">
      <el-card shadow="hover"
               style="margin-top:10px"
               v-for="user in userList"
               :key="user.account"
               @click.native="clickStudent(user)">
        <el-descriptions>
          <el-descriptions-item label="用户名">{{user.name}}</el-descriptions-item>
          <el-descriptions-item label="账号">{{user.account}}</el-descriptions-item>
          <el-descriptions-item label="电子邮箱">{{user.email}}</el-descriptions-item>
          <el-descriptions-item label="大学">{{user.college}}</el-descriptions-item>
          <el-descriptions-item label="专业">{{user.major}}</el-descriptions-item>
          <el-descriptions-item label="等级">
            <el-tag size="small">{{user.userLevel}}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-drawer title="我是标题"
                 :visible.sync="drawer"
                 :with-header="false"
                 size="60%"
                 direction="ltr">
        <router-view name="studentInfoView"
                     ref="routeView"></router-view>
      </el-drawer>
    </el-row>
  <el-backtop :bottom="60"><i class="el-icon-caret-top"></i></el-backtop>
  </div>
</template>
<script>
import request from '@/api/request'

export default {
  name: 'StudentInfo',
  data() {
    return {
      queryAccount: null,
      userLevel: '选择学生模式',
      chooseUserModel: 0,
      userNumber: 0,
      userList: [],
      drawer: false,
    }
  },

  watch: {
    drawer(oldValue) {
      if (!oldValue) {
        console.log('drawer变成false')
        //变为空白组件
        this.$router.push({
          name: 'BlankPage',
        })
      }
    },
  },

  methods: {
    //找到所有学生
    getAllStudent() {
      request({
        url: '/getAllStudent',
        method: 'post',
      }).then((res) => {
        this.userList = res.data
        this.userNumber = this.userList.length
      })
    },

    handleCommand(command) {
      //command就是每个下拉item的值
      this.userLevel = '模式' + command
      this.chooseUserModel = command

      //查找符合条件的用户
      request({
        url: '/getStudentInfoByUserModel',
        method: 'post',
        data: {
          chooseUserModel: this.chooseUserModel,
        },
      }).then((res) => {
        this.userList = res.data
        this.userNumber = this.userList.length
      })
    },

    queryStudentByAccount() {
      console.log('点击了查找学生')
      request({
        url: '/getInfo',
        method: 'post',
        data: {
          account: this.queryAccount,
        },
      }).then((res) => {
        console.log(res)
        if (res.code === 0) {
          this.$message({
            type: 'error',
            message: '没有这个人！',
          })
        } else {
          this.userList = []
          this.userList.push(res.data)
          this.userNumber = 1
        }
      })
    },

    clickStudent(user) {
      //   发送路由
      console.log(user.userId)
      this.$router.push({
        name: 'StudyCharts',
        params: {
          userId: user.userId,
        },
      })
      console.log(user)

      this.drawer = true
    },
  },

  mounted() {
    this.getAllStudent()
  },
}
</script>

<style scoped>
.el-row {
  width: 1000px;
  margin: auto;
}
</style>