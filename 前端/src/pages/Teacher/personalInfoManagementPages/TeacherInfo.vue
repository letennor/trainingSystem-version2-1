<template>
  <div>
   <el-card shadow="never" style="height: 600px">
     <div slot="header" class="clearfix" >
       <h4 style="margin: 0">个人中心</h4>
     </div>
     <el-tabs value="first">
       <el-tab-pane label="基本信息" name="first">
         <div style="width: 70%; margin-left: 50px;">
           <el-descriptions
            class="margin-top"
            :column="1"
            border
          >
            <template slot="extra">
              <el-button type="danger" size="small" @click="modifyPassword"
                >修改密码</el-button
              >
            </template>

            <template slot="extra">
              <el-button type="primary" size="small" @click="modifyEmail"
                >修改邮箱</el-button
              >
            </template>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-user"></i>
                用户名
              </template>
              {{ teacherName }}
            </el-descriptions-item>

            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-mobile-phone"></i>
                邮箱
              </template>
              {{ email }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-location-outline"></i>
                账号
              </template>
              {{ account }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-tickets"></i>
                备注
              </template>
              <el-tag size="small">老师</el-tag>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-office-building"></i>
                账号创建时间
              </template>
              {{ renderTime(createTime) }}
            </el-descriptions-item>
          </el-descriptions>
         </div>
         
       </el-tab-pane>
     </el-tabs>
   </el-card>
    <el-dialog title="修改邮箱" :visible.sync="isClickModifyEmail">
      <el-row>
        <el-col :span="24">
          <el-input placeholder="邮箱" v-model="newEmail">
            <template slot="prepend">邮箱</template>
          </el-input>
        </el-col>

        <el-col :span="24">
          <el-button
            type="success"
            plain
            @click="submitModifyEmail"
            style="margin-left: 613px"
            >确认修改</el-button
          >
        </el-col>
      </el-row>
    </el-dialog>

    <el-dialog title="修改密码" :visible.sync="isClickModifyPassword">
      <el-row>
        <el-col :span="24">
          <el-input placeholder="原始密码" v-model="originPassword">
            <template slot="prepend">原始密码</template>
          </el-input>
        </el-col>

        <el-col :span="24">
          <el-input placeholder="新密码" v-model="newPassword">
            <template slot="prepend">新密码</template>
          </el-input>
        </el-col>

        <el-col :span="24">
          <el-button
            type="success"
            plain
            @click="submitModifyPassword"
            style="margin-left: 613px"
            >确认修改</el-button
          >
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/api/request";
export default {
  name: "ModifyInfo",
  data() {
    return {
      isClickModifyPassword: false,
      isClickModifyEmail: false,
      newEmail: null,
      originPassword: null,
      newPassword: null,
      teacherInfo: null,
      teacherName: null,
      email: null,
      account: null,
      createTime: null,
    };
  },
  methods: {
      //取得老师信息
    getTeacherInfo() {
      request({
        url: "/getInfo",
        method: "post",
        data: {
          userId: sessionStorage.getItem("token"),
        },
      }).then((res) => {
        console.log("老师基本信息：", res);
        this.teacherInfo = res.data;
        this.teacherName = this.teacherInfo.name;
        this.email = this.teacherInfo.email;
        this.createTime = this.teacherInfo.gmtCreate;
        this.account = this.teacherInfo.account;
        this.newEmail = this.email;
      });
    },
    submitModifyEmail() {
      //修改邮箱
      request({
        url: "/student/message",
        method: "post",
        data: {
          userId: this.teacherInfo.userId,
          email: this.newEmail,
        },
      }).then((res) => {
        console.log(res);
        //刷新信息
        this.getTeacherInfo();
        this.$message({
          type: "success",
          message: "修改成功！",
        });

        this.isClickModifyEmail = false;
      });
    },

    submitModifyPassword() {
      request({
        url: "/modifyPassword",
        method: "post",
        data: {
          originPassword: this.originPassword,
          newPassword: this.newPassword,
          account: this.teacherInfo.account,
          userType: 2,
        },
      }).then((res) => {
        console.log(res);
        if (res.code === 0) {
          this.$message({
            type: "error",
            message: "输入的原始密码不正确!",
          });
        } else {
          this.$message({
            type: "success",
            message: "修改成功！",
          });
          this.isClickModifyPassword = false;
        }
      });
    },
    modifyPassword() {
      this.isClickModifyPassword = true;
    },

    modifyEmail() {
      this.isClickModifyEmail = true;
    },
    renderTime(date) {
      var dateee = new Date(date).toJSON();
      return new Date(+new Date(dateee) + 8 * 3600 * 1000)
        .toISOString()
        .replace(/T/g, " ")
        .replace(/\.[\d]{3}Z/, "");
    },
  },

  created() {
    this.getTeacherInfo();
  },
};
</script>

<style scoped>
.info-form {
  width: 500px;
  margin: 50px;
}
</style>
