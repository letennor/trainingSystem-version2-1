<template>
  <div>
    <el-row :gutter="40">
      <el-col :span="16">
        <el-card shadow="always"
                 style="height: 100%">
          <el-descriptions class="margin-top"
                           title="工作统计">
          </el-descriptions>
          <el-row>
            <el-col :span="10"
                    :offset="2"
                    style="margin-bottom: 0">
              <!-- 题目统计 -->
              <div id="questionStatistic"
                   style="width: 300px; height: 280px"></div>
            </el-col>

            <el-col :span="10"
                    style="margin-bottom: 0">
              <!-- 专项统计 -->
              <div id="specialTestStatistic"
                   style="width: 300px; height: 280px"></div>
            </el-col>
            <el-col :span="10"
                    :offset="2">
              <div id="testSetStatistic"
                   style="width: 300px; height: 280px"></div>
            </el-col>

            <el-col :span="10">
              <div id="messageStatistic"
                   style="width: 300px; height: 280px"></div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="always">
          <el-descriptions class="margin-top"
                           title="系统公告">
          </el-descriptions>
          <el-empty description="暂无公告"
                    style="padding: 0"
                    v-if="notificationList.length == 0"></el-empty>
          <el-carousel height="225px"
                       :interval="3000"
                       :autoplay="true"
                       indicator-position="none"
                       v-if="notificationList.length != 0">
            <el-carousel-item v-for="notification in notificationList"
                              :key="notification.notificationId">
              <el-col :span="24">
                {{ notification.notificationInfo }}
              </el-col>
              <el-col :span="24">
                <el-button type="primary"
                           style="position: absolute; top: 180px; margin-left: 115px"
                           @click="confirmNotification(notification)">确认</el-button>
              </el-col>
            </el-carousel-item>
          </el-carousel>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog title="修改邮箱"
               :visible.sync="isClickModifyEmail">
      <el-row>
        <el-col :span="24">
          <el-input placeholder="邮箱"
                    v-model="newEmail">
            <template slot="prepend">邮箱</template>
          </el-input>
        </el-col>

        <el-col :span="24">
          <el-button type="success"
                     plain
                     @click="submitModifyEmail"
                     style="margin-left: 613px">确认修改</el-button>
        </el-col>
      </el-row>
    </el-dialog>

    <el-dialog title="修改密码"
               :visible.sync="isClickModifyPassword">
      <el-row>
        <el-col :span="24">
          <el-input placeholder="原始密码"
                    v-model="originPassword">
            <template slot="prepend">原始密码</template>
          </el-input>
        </el-col>

        <el-col :span="24">
          <el-input placeholder="新密码"
                    v-model="newPassword">
            <template slot="prepend">新密码</template>
          </el-input>
        </el-col>

        <el-col :span="24">
          <el-button type="success"
                     plain
                     @click="submitModifyPassword"
                     style="margin-left: 613px">确认修改</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>
<script>
import request from '../../../api/request'
var echarts = require('echarts')
export default {
  name: 'BasicInfo',
  data() {
    return {
      teacherInfo: null,
      teacherName: null,
      email: null,
      account: null,
      createTime: null,
      notificationList: [],
      isClickModifyPassword: false,
      isClickModifyEmail: false,
      newEmail: null,
      originPassword: null,
      newPassword: null,

      allMessageNumber: null,
      allQuestionNumber: null,
      allSpecialNumber: null,
      allTestSetNumber: null,
      myMessageNumber: null,
      myQuestionNumber: null,
      mySpecialTestNumber: null,
      myTestSetNumber: null,
    }
  },

  methods: {
    //取得老师信息
    getTeacherInfo() {
      request({
        url: '/getInfo',
        method: 'post',
        data: {
          userId: sessionStorage.getItem('token'),
        },
      }).then((res) => {
        console.log('老师基本信息：', res)
        this.teacherInfo = res.data
        this.teacherName = this.teacherInfo.name
        this.email = this.teacherInfo.email
        this.createTime = this.teacherInfo.gmtCreate
        this.account = this.teacherInfo.account
        this.newEmail = this.email
      })
    },

    //接收通知信息
    getNotification() {
      request({
        url: '/getNotificationByUserId',
        method: 'post',
        data: {
          userId: sessionStorage.getItem('token'),
        },
      }).then((res) => {
        this.notificationList = res.data
      })
    },

    confirmNotification(notification) {
      request({
        url: '/confirmNotification',
        method: 'post',
        data: {
          notificationId: notification.notificationId,
        },
      }).then((res) => {
        console.log(res)
        //刷新通知
        this.getNotification()
        this.$message({
          type: 'success',
          message: '通知已确认！',
        })
      })
    },

    modifyPassword() {
      this.isClickModifyPassword = true
    },

    modifyEmail() {
      this.isClickModifyEmail = true
    },

    submitModifyEmail() {
      //修改邮箱
      request({
        url: '/student/message',
        method: 'post',
        data: {
          userId: this.teacherInfo.userId,
          email: this.newEmail,
        },
      }).then((res) => {
        console.log(res)
        //刷新信息
        this.getTeacherInfo()
        this.$message({
          type: 'success',
          message: '修改成功！',
        })

        this.isClickModifyEmail = false
      })
    },

    submitModifyPassword() {
      request({
        url: '/modifyPassword',
        method: 'post',
        data: {
          originPassword: this.originPassword,
          newPassword: this.newPassword,
          account: this.teacherInfo.account,
          userType: 2,
        },
      }).then((res) => {
        console.log(res)
        if (res.code === 0) {
          this.$message({
            type: 'error',
            message: '输入的原始密码不正确!',
          })
        } else {
          this.$message({
            type: 'success',
            message: '修改成功！',
          })
          this.isClickModifyPassword = false
        }
      })
    },

    getTeacherStatisticInfo() {
      //请求老师的统计信息
      request({
        url: '/getTeacherStatisticInfo',
        method: 'post',
        data: {
          teacherId: sessionStorage.getItem('token'),
        },
      }).then((res) => {
        console.log(res)
        let data = res.data
        console.log(data)
        this.allMessageNumber = data.allMessageNumber
        this.allQuestionNumber = data.allQuestionNumber
        this.allSpecialNumber = data.allSpecialNumber
        this.allTestSetNumber = data.allTestSetNumber
        this.myMessageNumber = data.myMessageNumber
        this.myQuestionNumber = data.myQuestionNumber
        this.mySpecialTestNumber = data.mySpecialTestNumber
        this.myTestSetNumber = data.myTestSetNumber
        this.opstionsQuestionStatistic()
        this.opstionsSpecialTestStatistic()
        this.opstionsTestSetStatistic()
        this.opstionsMessageStatistic()
      })
    },

    renderTime(date) {
      var dateee = new Date(date).toJSON()
      return new Date(+new Date(dateee) + 8 * 3600 * 1000)
        .toISOString()
        .replace(/T/g, ' ')
        .replace(/\.[\d]{3}Z/, '')
    },

    opstionsQuestionStatistic() {
      let myChart = echarts.init(document.getElementById('questionStatistic'))
      let option1 = {
        title: {
          text: this.myQuestionNumber + '/' + this.allQuestionNumber,
          left: 'center',
          top: 'center',
        },

        tooltip: {
          //鼠标放到图上的数据展示样式
          trigger: 'item',
          formatter: '{b}:{c}({d}%)',
        },

        color: ['#546570', '#c4ccd3'],
        series: [
          {
            type: 'pie',
            data: [
              {
                value: this.myQuestionNumber,
                name: '我的题目',
              },
              {
                value: this.allQuestionNumber - this.myQuestionNumber,
                name: '其他题目',
              },
            ],
            radius: ['40%', '70%'],
          },
        ],
      }
      myChart.setOption(option1) // echarts设置选项
      window.onresize = function () {
        myChart.resize()
      }
    },

    opstionsSpecialTestStatistic() {
      let myChart = echarts.init(
        document.getElementById('specialTestStatistic')
      )
      let option1 = {
        title: {
          text: this.mySpecialTestNumber + '/' + this.allSpecialNumber,
          left: 'center',
          top: 'center',
        },
        tooltip: {
          //鼠标放到图上的数据展示样式
          trigger: 'item',
          formatter: '{b}:{c}({d}%)',
        },
        color: ['#73b9bc', '#7289ab'],
        series: [
          {
            type: 'pie',
            data: [
              {
                value: this.mySpecialTestNumber,
                name: '我的专项',
              },
              {
                value: this.allSpecialNumber - this.mySpecialTestNumber,
                name: '其他专项',
              },
            ],
            radius: ['40%', '70%'],
          },
        ],
      }
      myChart.setOption(option1) // echarts设置选项
      window.onresize = function () {
        myChart.resize()
      }
    },

    opstionsTestSetStatistic() {
      let myChart = echarts.init(document.getElementById('testSetStatistic'))
      let option1 = {
        title: {
          text: this.myTestSetNumber + '/' + this.allTestSetNumber,
          left: 'center',
          top: 'center',
        },
        tooltip: {
          //鼠标放到图上的数据展示样式
          trigger: 'item',
          formatter: '{b}:{c}({d}%)',
        },
        series: [
          {
            type: 'pie',
            data: [
              {
                value: this.myTestSetNumber,
                name: '我的套题',
              },
              {
                value: this.allTestSetNumber - this.myTestSetNumber,
                name: '其他套题',
              },
            ],
            radius: ['40%', '70%'],
          },
        ],
      }
      myChart.setOption(option1) // echarts设置选项
      window.onresize = function () {
        myChart.resize()
      }
    },

    opstionsMessageStatistic() {
      let myChart = echarts.init(document.getElementById('messageStatistic'))
      let option1 = {
        title: {
          text: this.myMessageNumber + '/' + this.allMessageNumber,
          left: 'center',
          top: 'center',
        },
        tooltip: {
          //鼠标放到图上的数据展示样式
          trigger: 'item',
          formatter: '{b}:{c}({d}%)',
        },
        color: ['#ea7e53', '#eedd78'],
        series: [
          {
            type: 'pie',
            data: [
              {
                value: this.myMessageNumber,
                name: '我的回答',
              },
              {
                value: this.allMessageNumber - this.myMessageNumber,
                name: '其他回答',
              },
            ],
            radius: ['40%', '70%'],
          },
        ],
      }
      myChart.setOption(option1) // echarts设置选项
      window.onresize = function () {
        myChart.resize()
      }
    },
  },

  mounted() {
    this.getTeacherInfo()
    this.getNotification()
    this.getTeacherStatisticInfo()
  },
}
</script>
<style scoped>
.el-card {
  height: 300px;
}

.el-col {
  margin-bottom: 20px;
}
</style>
