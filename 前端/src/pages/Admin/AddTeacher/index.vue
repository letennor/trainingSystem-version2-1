<template>
  <div>
    <div class="info-form">
      <el-form
        :model="infoForm"
        label-width="100px"
        :rules="rules"
        ref="teacher"
      >
        <el-form-item label="姓名" prop="name">
          <el-input
            v-model="infoForm.name"
            placeholder="请输入教师姓名"
          ></el-input>
        </el-form-item>
        <el-form-item label="账号" prop="account">
          <el-input
            v-model="infoForm.account"
            placeholder="请输入账号"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pwd">
          <el-input v-model="infoForm.pwd" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="学校" prop="college">
          <el-input
            v-model="infoForm.college"
            placeholder="请输入学校名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input
            v-model="infoForm.major"
            placeholder="请输入专业名称"
          ></el-input>
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="infoForm.email"
            placeholder="请输入邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click.once="addTeacher">创建账号</el-button>
          <el-button @click="$refs.teacher.resetFields()">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/api/request";
export default {
  name: "AddTeacher",
  data() {
    let checkAccount = (rule, value, cb) => {
      const regAccount = /^[1-9]\d*$/;
      if (regAccount.test(value)) {
        return cb()
      }
      cb(new Error('学号必须为纯数字且不能以零开头'))
    }
    return {
      infoForm: {
        name: "",
        college: "",
        major: "",
        account: "",
        pwd: "",
        email: "",
      },
      rules: {
        name: [{ required: true, message: "请输入教师姓名", trigger: "blur" }],
        account: [
          { required: true, message: "请输入账号", trigger: "blur" },
          { validator: checkAccount, message: "账号必须为纯数字且不能以零开头", trigger: "blur" },
          { min: 6, max: 10, message: "账号必须为6~10位", trigger: "blur" },
        ],
        pwd: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 8, max: 16, message: "密码必须为8~16位", trigger: "blur" },
        ],
        college: [
          { required: true, message: "请输入学校名称", trigger: "blur" },
        ],
        major: [{ required: true, message: "请输入专业名称", trigger: "blur" }],
        email: [
          { required: true, message: "请输入邮箱地址", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur"],
          },
        ],
      },
    };
  },
  methods: {
    addTeacher() {
      this.$refs.teacher.validate((valid) => {
        if (valid) {
          request({
            url: "/admin/addTeacher",
            method: "POST",
            data: this.infoForm,
          }).then((res) => {
            console.log(res);
            if (res.code == 200) {
              this.$message({
                message: "添加成功。账号：" + this.infoForm.account,
                type: "success",
              });
            } else {
              this.$message({
                message: res.msg,
                type: "error",
              });
            }
          });
        }
      });
    },
  },
};
</script>

<style></style>
