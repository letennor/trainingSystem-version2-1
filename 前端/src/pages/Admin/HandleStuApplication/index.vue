<template>
  <div>
    <div
      v-loading="!isReady"
      element-loading-background="#ffffff"
      element-loading-text="正在加载"
    >
      <el-empty description="暂无学生申请" v-if="isReady && !stuAskList.length"></el-empty>
      <el-row class="row-info" v-else>
        <el-card
          shadow="hover"
          style="margin-top: 10px"
          v-for="(stuAsk, index) in stuAskList"
          :key="index"
        >
          <div class="card-ask">
            <el-descriptions :column="2">
              <el-descriptions-item label="申请id">{{
                stuAsk.notificationId
              }}</el-descriptions-item>
              <el-descriptions-item label="学生id">{{
                stuAsk.userId
              }}</el-descriptions-item>
              <el-descriptions-item label="内容">{{
                stuAsk.notificationInfo
              }}</el-descriptions-item>
            </el-descriptions>
          </div>

          <div class="check-ask">
            <el-button type="primary" size="small" @click="setAskInfo(stuAsk)"
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
              {{ studentInfo.studentName }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-mobile-phone"></i>
                学号
              </template>
              {{ studentInfo.studentId }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-location-outline"></i>
                学校
              </template>
              {{ studentInfo.schoolName }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-tickets"></i>
                专业
              </template>
              {{ studentInfo.major }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-office-building"></i>
                等级
              </template>
              <el-tag size="small">{{ studentInfo.level }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-office-building"></i>
                邮箱
              </template>
              {{ studentInfo.email }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-office-building"></i>
                申请内容
              </template>
              {{ studentInfo.askInfo }}
            </el-descriptions-item>
          </el-descriptions>
          <el-form :model="replyInfo" ref="askResult" :rules="rules">
            <el-form-item label="是否同意" prop="result">
              <el-radio-group v-model="replyInfo.result" @change="changeRadio">
                <el-radio-button label="0">拒绝</el-radio-button>
                <el-radio-button label="1">同意</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="请调整该学生的等级" v-if="replyInfo.result.length > 0 && replyInfo.result == 1">
              <el-input-number
                :min="2"
                :max="5"
                v-model="replyInfo.newLevel"
              ></el-input-number>
            </el-form-item>
            <el-form-item prop="reply" v-if="replyInfo.result.length > 0 && replyInfo.result == 0">
              <el-input
                v-model="replyInfo.reply"
                placeholder="请输入回复"
              ></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <div style="float: left">
              <el-button
                type="primary"
                @click.native="clickStudent(selectAskUserId)"
                >查看学生训练数据</el-button
              >
            </div>
            <el-button @click="handleAskDialog = false">取 消</el-button>
            <el-button type="primary" @click="submitResult">提交审核</el-button>
            <div class="message"><span>*点击外侧区域即可返回</span></div>
          </span>
        </el-dialog>
        <el-drawer
          title="我是标题"
          :visible.sync="drawer"
          :with-header="false"
          size="60%"
          direction="ltr"
          @close="drawerClose"
        >
          <router-view></router-view>
        </el-drawer>
      </el-row>
      <el-backtop :bottom="60"><i class="el-icon-caret-top"></i></el-backtop>
    </div>
  </div>
</template>

<script>
import request from "@/api/request";
export default {
  name: "HandleStuApplication",
  data() {
    return {
      stuAskList: [],
      handleAskDialog: false,
      selectAskId: "",
      selectAskUserId: "",
      studentInfo: {
        studentName: "",
        studentId: "",
        schoolName: "",
        major: "",
        level: 0,
        email: "",
        askInfo: "",
      },
      replyInfo: {
        result: "", // 审核结果（是否同意）
        reply: "", // 回复信息
        newLevel: "", // 新的等级
      },
      rules: {
        result: [
          { required: true, message: "请选择审核结果", trigger: "blur" },
        ],
        reply: [
          {required: true, message: "请输入回复"}
        ]
      },
      // 控制抽屉
      drawer: false,
      isReady: false, // 请求是否完成
    };
  },
  methods: {
    // 修改单选按钮时触发
    changeRadio() {
      this.$refs.askResult.clearValidate();
    },
    // 展示对话框中的学生信息
    setAskInfo(stuAsk) {
      this.selectAskId = stuAsk.notificationId;
      this.selectAskUserId = stuAsk.userId;
      this.studentInfo.askInfo = stuAsk.notificationInfo;
      console.log(this.selectAskId);
      console.log(this.selectAskUserId);
      request({
        url: "/getInfo",
        method: "POST",
        data: {
          userId: stuAsk.userId,
        },
      }).then((res) => {
        // console.log(res);
        this.studentInfo.studentName = res.data.name;
        this.studentInfo.studentId = res.data.account;
        this.studentInfo.schoolName = res.data.college;
        this.studentInfo.major = res.data.major;
        this.studentInfo.level = res.data.userLevel;
        this.studentInfo.email = res.data.email;
        this.replyInfo.newLevel = res.data.userLevel;
      });
      this.handleAskDialog = true;
    },
    // 对话框关闭时的回调
    closeAlert() {
      this.selectAskUserId = "";
      this.studentInfo.studentName = "";
      this.studentInfo.studentId = "";
      this.studentInfo.schoolName = "";
      this.studentInfo.major = "";
      this.studentInfo.level = "";
      this.studentInfo.email = "";
      this.studentInfo.askInfo = "";
      this.selectAskId = "";
      this.replyInfo.result = "";
      this.replyInfo.reply = "";
      this.replyInfo.newLevel = "";
      this.$refs.askResult.clearValidate();
    },
    // 提交审核结果
    submitResult() {
      this.$refs.askResult.validate((valid) => {
        if (valid) {
          request({
            url: "/admin/checkStuApplication",
            method: "POST",
            data: {
              notificationId: this.selectAskId,
              studentId: this.selectAskUserId,
              decision: this.replyInfo.result,
              reply: this.replyInfo.result == 1 ? '管理员已同意你的更换模式申请' : this.replyInfo.reply,
              newLevel: this.replyInfo.newLevel,
              adminId: window.sessionStorage.getItem("token"),
            },
          }).then((res) => {
            console.log(res);
            if (res.code == 1) {
              this.$message({
                message: "更改成功！已给学生发放通知",
                type: "success",
              });
            } else if (res.code == 0) {
              this.$message({
                message: "提交成功，已给学生发放通知",
                type: "success",
              });
            }
            this.getStuApplicationInfo();
          });
          this.handleAskDialog = false;
          this.$bus.$emit("countSub", "stuAskCount");
        }
      });
    },
    clickStudent(userId) {
      this.drawer = true;
      //   发送路由
      console.log(userId);
      this.$router.push({
        name: "StudyCharts1",
        // path: 'handleStuApplication/studentCharts',
        params: {
          userId: userId,
        },
      });
    },
    // drawer关闭时的回调
    drawerClose() {
      console.log("drawer关闭了");
      this.$router.push("/adminHome/handleStuApplication");
    },
    getStuApplicationInfo() {
      request({
        url: "/admin/studentAsk",
        method: "POST",
        data: { adminId: window.sessionStorage.getItem("token") },
      }).then((res) => {
        if (res.code == 200) {
          this.stuAskList = [];
          res.data.forEach((ask) => {
            if (ask.isCheck == 0) {
              this.stuAskList.push(ask);
            }
          });
          this.isReady = true;
        }
      });
    },
  },
  created() {
    // 发送请求获得申请列表
    this.getStuApplicationInfo();
  },
  mounted() {},
};
</script>

<style scoped>
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
