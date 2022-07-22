<template>
  <div v-loading="!isReady" element-loading-background="#ffffff" element-loading-text="正在加载">
    <el-empty description="暂无老师申请" v-if="isReady && !teaAskList.length"></el-empty>
    <el-row class="row-info" v-else>
      <el-card
        shadow="hover"
        style="margin-top: 10px"
        v-for="(teaAsk, index) in teaAskList"
        :key="index"
      >
        <div class="card-ask">
          <el-descriptions :column="2">
            <el-descriptions-item label="申请id">{{
              teaAsk.notificationId
            }}</el-descriptions-item>
            <el-descriptions-item label="老师id">{{
              teaAsk.userId
            }}</el-descriptions-item>
            <el-descriptions-item label="内容">{{
              teaAsk.notificationInfo
            }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="check-ask">
          <el-button type="primary" size="small" @click="setAskInfo(teaAsk)"
            >查看</el-button
          >
        </div>
      </el-card>
      <el-dialog
        title="申请详情"
        :visible.sync="handleAskDialog"
        class="delete-dialog"
        @close="closeAlert"
        top="10vh"
      >
        <el-descriptions
          class="margin-top"
          :column="2"
          border
          title="学生信息"
          size="medium"
        >
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-user"></i>
              姓名
            </template>
            {{ teacherInfo.name }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-mobile-phone"></i>
              账号
            </template>
            {{ teacherInfo.account }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-location-outline"></i>
              学校
            </template>
            {{ teacherInfo.college }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-tickets"></i>
              专业
            </template>
            {{ teacherInfo.major }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-office-building"></i>
              邮箱
            </template>
            {{ teacherInfo.email }}
          </el-descriptions-item>
          <el-descriptions-item> </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-office-building"></i>
              申请内容
            </template>
            {{ teacherInfo.askInfo }}
          </el-descriptions-item>
        </el-descriptions>
        <el-form :model="replyInfo" ref="askResult" style="margin-top: 10px">
          <el-form-item label="请输入回复：">
            <el-input
              v-model="replyInfo.reply"
              placeholder="回复内容"
            ></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="handleAskDialog = false">取 消</el-button>
          <el-button type="primary" @click="submitResult">提交</el-button>
          <div class="message"><span>*点击外侧区域即可返回</span></div>
        </span>
      </el-dialog>
    </el-row>
    <el-backtop :bottom="60"><i class="el-icon-caret-top"></i></el-backtop>
  </div>
</template>

<script>
import request from "@/api/request";
export default {
  name: "HandleTeaApplication",
  data() {
    return {
      teaAskList: [],
      handleAskDialog: false,
      selectAskId: "",
      selectAskUserId: "",
      teacherInfo: {
        name: "",
        account: "",
        email: "",
        college: "",
        major: "",
        askInfo: "",
      },
      replyInfo: {
        reply: "",
      },
      isReady: false,
    };
  },
  methods: {
    setAskInfo(teaAsk) {
      this.selectAskId = teaAsk.notificationId;
      this.selectAskUserId = teaAsk.userId;
      this.teacherInfo.askInfo = teaAsk.notificationInfo;
      request({
        url: "/getInfo",
        method: "POST",
        data: {
          userId: teaAsk.userId,
        },
      }).then((res) => {
        // console.log(res);
        this.teacherInfo.name = res.data.name;
        this.teacherInfo.account = res.data.account;
        this.teacherInfo.college = res.data.college;
        this.teacherInfo.major = res.data.major;
        this.teacherInfo.email = res.data.email;
      });
      this.handleAskDialog = true;
    },
    closeAlert() {
      this.handleAskDialog = false;
      this.teacherInfo.name = "";
      this.teacherInfo.account = "";
      this.teacherInfo.college = "";
      this.teacherInfo.major = "";
      this.teacherInfo.email = "";
      this.replyInfo.reply = "";
      this.selectAskId = '';
      this.selectAskUserId = '';
    },
    submitResult() {
      request({
        url: '/admin/checkTeaApplication',
        method: 'POST',
        data: {
          notificationId: this.selectAskId,
          teacherId: this.selectAskUserId,
          reply: this.replyInfo.reply,
          adminId: sessionStorage.getItem('token')
        }
      }).then(res => {
        if (res.code == 0) {
          this.$message({
            message: '已给教师发放反馈通知',
            type: 'success'
          })
          this.handleAskDialog = false;
          this.$bus.$emit('countSub', 'teaAskCount');
          this.getTeaApplicationInfo();
        }
      })
    },
    getTeaApplicationInfo() {
      request({
      url: "/admin/teacherAsk",
      method: "POST",
      data: { adminId: window.sessionStorage.getItem("token") },
    }).then((res) => {
      if (res.code == 200) {
        this.teaAskList = [];
        res.data.forEach((ask) => {
          if (ask.isCheck == 0) {
            this.teaAskList.push(ask);
          }
        });
        this.isReady = true;
      }
    });
    }
  },
  created() {
    this.getTeaApplicationInfo();
  },
};
</script>

<style>
.row-info {
  width: 1200px;
  margin: auto;
  margin-top: 20px;
}
.row-info .check-ask {
  float: right;
  line-height: 35px;
}
.row-info .card-ask {
  float: left;
  width: 1100px;
}
.message {
  margin-top: 10px;
  font-size: 12px;
  color: rgb(103, 119, 128);
}
</style>
