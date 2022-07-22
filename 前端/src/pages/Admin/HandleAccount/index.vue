<template>
  <div>
    <el-row style="line-height: 50px">
      <el-col :span="6">
        <el-select
          v-model="userRadio"
          placeholder="请选择用户类型"
          @change="chooseUserType"
        >
          <el-option label="学生用户" value="1"></el-option>
          <el-option label="教师用户" value="2"></el-option>
          <el-option label="管理员用户" value="3"></el-option>
        </el-select>
      </el-col>
      <el-col :span="6">
        <el-select
          v-if="userRadio == 1"
          v-model="userLevel"
          placeholder="按学生等级筛选"
          @change="chooseStuLevel"
        >
          <el-option label="等级0" value="0"></el-option>
          <el-option label="等级2" value="2"></el-option>
          <el-option label="等级3" value="3"></el-option>
          <el-option label="等级4" value="4"></el-option>
          <el-option label="等级5" value="5"></el-option>
        </el-select>
      </el-col>
      <el-col :span="12" style="float: right">
        <el-input
          placeholder="输入用户账号"
          v-model="queryAccount"
          class="input-with-select"
        >
          <el-button
            type="primary"
            slot="append"
            @click="queryUserByAccount"
            icon="el-icon-search"
          ></el-button>
        </el-input>
      </el-col>
    </el-row>
    <el-row class="row-info">
      <el-card
        shadow="hover"
        style="margin-top: 10px"
        v-for="(user, index) in showUserList"
        :key="index"
      >
        <div class="card-info">
          <el-descriptions>
            <el-descriptions-item label="用户名">{{
              user.name
            }} <el-tag type="success" size="mini">{{`${userRadio == 1 || user.userType == 1 ? '学生' : userRadio == 2 || user.userType == 2 ? '教师' : '管理员'}`}}</el-tag></el-descriptions-item>
            <el-descriptions-item label="账号">{{
              user.account
            }}</el-descriptions-item>
            <el-descriptions-item label="电子邮箱">{{
              user.email
            }}</el-descriptions-item>
            <el-descriptions-item label="大学">{{
              user.college
            }}</el-descriptions-item>
            <el-descriptions-item label="专业" v-if="userRadio != 3">{{
              user.major
            }}</el-descriptions-item>
            <el-descriptions-item label="等级" v-if="userRadio == 1 || user.userType == 1">
              <el-tag size="small">{{ user.userLevel }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="delete-user" v-if="userRadio == 1 || showDeleteBtn == true">
          <el-button
            type="danger"
            size="small"
            @click="setDeleteId(user)"
            >删除</el-button
          >
        </div>
      </el-card>
      <el-dialog
        title="提示"
        :visible.sync="deleteAlert"
        class="delete-dialog"
        @close="closeAlert"
      >
        <span>确定要删除该用户吗？确定后该用户的所有信息都将失去</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="deleteAlert = false">取 消</el-button>
          <el-button type="primary" @click="deleteUser">确 定</el-button>
        </span>
      </el-dialog>
    </el-row>
    <el-backtop :bottom="60"><i class="el-icon-caret-top"></i></el-backtop>
    <el-empty
      description="请选择上方条件查看用户"
      v-if="
        userRadio == '' &&
        userLevel == '' &&
        queryAccount == '' &&
        showUserList.length == 0
      "
    ></el-empty>
    <el-empty
      description="没有符合条件的用户"
      v-else-if="showUserList.length == 0"
    ></el-empty>
  </div>
</template>

<script>
import request from "@/api/request";
export default {
  name: "HandleAccount",
  data() {
    return {
      userRadio: "",
      userLevel: "",
      showUserList: [],
      queryAccount: "",
      deleteAlert: false,
      // 选中的deleteid
      deleteUserId: "",
      wantDeleteUser: '',
      showDeleteBtn: false,
    };
  },
  methods: {
    // 用户类型选择器绑定的事件
    chooseUserType(choose) {
      console.log(choose);
      this.userLevel = "";
      this.queryAccount = "";
      this.showDeleteBtn = false;
      request({
        url: "/admin/allAccount",
        method: "POST",
        data: { userType: choose },
      }).then((res) => {
        console.log(res);
        if (res.code == 200) {
          this.showUserList = res.data;
        }
      });
    },
    // 学生等级选择器绑定的事件
    chooseStuLevel(choose) {
      request({
        url: "/getStudentInfoByUserLevel",
        method: "POST",
        data: { userLevel: choose },
      }).then((res) => {
        console.log(res);
        if (res.code == 200) {
          this.showUserList = res.data;
        }
      });
    },
    // 通过账号查询用户
    queryUserByAccount() {
      this.userRadio = "";
      this.userlevel = "";
      if (this.queryAccount.trim() != "") {
        request({
          url: "/getInfo",
          method: "post",
          data: {
            account: this.queryAccount,
            userId: window.sessionStorage.getItem("token"),
          },
        }).then((res) => {
          console.log(res);
          if (res.code == 200) {
            if (res.data.userType == 1) {
              this.showDeleteBtn = true;
            } else {
              this.showDeleteBtn = false
            }
            this.showUserList = [];
            if (res.data.userId != sessionStorage.getItem("token")) {
              this.showUserList.push(res.data);
            }
          } else {
            this.$message({
              message: "未找到该用户",
              type: "warning",
            });
            this.showUserList = [];
          }
        });
      } else {
        this.$message({
          message: "请输入合法的账号！",
          type: "warning",
        });
      }
    },
    setDeleteId(user) {
      console.log(user);
      this.deleteAlert = true;
      this.wantDeleteUser = user;
    },
    deleteUser() {
      request({
        url: "/deleteAccount",
        method: "POST",
        // data: this.deleteUserId
        data: { userId: this.wantDeleteUser.userId, account: this.wantDeleteUser.account },
      }).then((res) => {
        if (res.code == 200) {
          this.$message({
            message: "删除成功！",
            type: "success",
          });
          if (this.userRadio != "") {
            if (this.userLevel != "") {
              this.chooseStuLevel(this.userLevel);
            } else {
              this.chooseUserType(this.userRadio);
            }
          } else if (this.queryAccount != "") {
            console.log("queryAcc");
            this.showUserList = [];
          }
        } else {
          this.$message({
            message: '注销失败',
            type: 'warning'
          })
        }
      });
      this.deleteAlert = false;
    },
    closeAlert() {
      this.deleteUserId = "";
    },
  },
};
</script>

<style scoped>
.row-info {
  width: 1200px;
  margin: auto;
  margin-top: 20px;
}
.row-info .delete-user {
  float: right;
  line-height: 35px;
}
.row-info .card-info {
  float: left;
  width: 1100px;
}
.delete-dialog {
  margin: auto;
}
</style>
