<template>
  <div>
    <el-card shadow="never">
      <div slot="header"
           class="clearfix">
        <h4 style="margin: 0">个人中心</h4>
      </div>
      <el-tabs v-model="activeName"
               @tab-click="handleClick">
        <el-tab-pane label="基本信息"
                     name="first">
          <div style="width: 70%; margin-left: 50px"
               v-if="!modifyInfo">
            <el-descriptions title=""
                             class="margin-top"
                             :column="1"
                             border>
              <template slot="extra">
                <el-button-group>
                  <el-button type="primary"
                             icon="el-icon-edit"
                             @click="modifyInfo = true">修改信息</el-button>
                </el-button-group>
              </template>
              <el-descriptions-item>
                <template slot="label">
                  <i class="el-icon-user"></i>
                  姓名
                </template>
                {{ studentInfo.studentName }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  <i class="el-icon-postcard"></i>
                  学号
                </template>
                {{ studentInfo.studentId }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  <i class="el-icon-office-building"></i>
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
                  <i class="el-icon-coin"></i>
                  等级
                </template>
                <el-tag size="small">{{ studentInfo.level }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label">
                  <i class="el-icon-message"></i>
                  邮箱
                </template>
                {{ studentInfo.email }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
          <div class="info-form">

            <el-form :model="infoForm"
                     label-width="100px"
                     v-if="modifyInfo">
              <el-form-item label="姓名"
                            prop="studentName"
                            :rules="[{ required: true, message: '请输入姓名', trigger: 'blur' }]">
                <el-input v-model="infoForm.studentName"
                          placeholder="请输入姓名"></el-input>
              </el-form-item>
              <el-form-item label="学号"
                            prop="studentId"
                            :rules="[{ required: true, message: '请输入学号', trigger: 'blur' }]">
                <el-input v-model="infoForm.studentId"
                          disabled></el-input>
              </el-form-item>
              <el-form-item label="学校"
                            prop="schoolName"
                            :rules="[
            { required: true, message: '请输入学校名称', trigger: 'blur' },
          ]">
                <el-input v-model="infoForm.schoolName"
                          placeholder="请输入学校名称"></el-input>
              </el-form-item>
              <el-form-item label="专业"
                            prop="major"
                            :rules="[
            { required: true, message: '请输入专业名称', trigger: 'blur' },
          ]">
                <el-input v-model="infoForm.major"
                          placeholder="请输入专业名称"></el-input>
              </el-form-item>
              <el-form-item label="等级"
                            prop="level"
                            :rules="[{ required: true, message: '请输入等级', trigger: 'blur' }]">
                <el-input v-model="infoForm.level"
                          disabled></el-input>
              </el-form-item>
              <el-form-item label="邮箱"
                            prop="email"
                            :rules="[
            { required: true, message: '请输入邮箱地址', trigger: 'blur' },
            {
              type: 'email',
              message: '请输入正确的邮箱地址',
              trigger: ['blur', 'change'],
            },
          ]">
                <el-input v-model="infoForm.email"
                          placeholder="请输入邮箱"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary"
                           @click="submitForm">保存</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="密码管理"
                     name="second">
          <div class="info-form">
            <el-form :model="modifypwd"
                     ref="modifyPwd"
                     :rules="rules1"
                     label-width="150px">
              <el-form-item label="旧密码"
                            label-width="120px"
                            prop="oldPwd">
                <el-input type="password"
                          v-model="modifypwd.oldPwd"
                          :show-password="true"
                          placeholder="请输入原始密码"
                          autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="新密码"
                            label-width="120px"
                            prop="newPwd">
                <el-input type="password"
                          v-model="modifypwd.newPwd"
                          :show-password="true"
                          placeholder="请输入新密码"
                          autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="确认新密码"
                            label-width="120px"
                            prop="newPwd1">
                <el-input type="password"
                          v-model="modifypwd.newPwd1"
                          :show-password="true"
                          placeholder="请再次输入新密码"
                          autocomplete="off"></el-input>
              </el-form-item>

            </el-form>
            <el-button style="margin-left: 120px"
                       type="primary"
                       @click="sendPwd('modifyPwd')">修改</el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane label="申请更换模式"
                     name="third">
          <div class="info-form">
            <el-form :model="changeModel"
                     ref="changeModel"
                     :rules="rules2">
              <el-form-item label="请选择模式"
                            label-width="120px"
                            prop="newModel">
                <el-radio v-model="changeModel.newModel"
                          label="模式一">
                  <el-card style="width: 600px">
                    <div slot="header"
                         class="clearfix">
                      <span> 模式一</span>
                    </div>
                    此模式针对基础较薄弱学生。
                    <br><br>训练模式为：首先进行知
                    识点学习，学习之后再通过配套练习来检验掌握情况

                  </el-card>
                </el-radio>
                <el-radio v-model="changeModel.newModel"
                          label="模式二">
                  <el-card style="width: 600px">
                    <div slot="header"
                         class="clearfix">
                      <span> 模式二</span>
                    </div>
                    <p> 此模式针对有一定基础的学生。
                      <br><br>训练模式为：对学生给出系统的训练计划，学生依次完成计划的套题。
                    </p>

                  </el-card>
                </el-radio>
                <el-radio v-model="changeModel.newModel"
                          label="模式三"
                          :disabled="disableModel3">
                  <el-card style="width: 600px">
                    <div slot="header"
                         class="clearfix">
                      <span> 模式三</span>
                    </div>
                    此模式针对基础较好，只是想针对某些薄弱知识点进行巩固学习的学生。
                    <br><br>训练模式为：首先，学生勾选想要专项学习的知识点，
                    <br><br>然后系统针对该学生推出专门的专项练习计划，学生依次完成训练计划中的套题。

                  </el-card>
                </el-radio>
              </el-form-item>
              <el-form-item label="更换理由"
                            label-width="120px"
                            prop="reason">
                <el-input style="width: 600px"
                          type="textarea"
                          :rows="6"
                          v-model="changeModel.reason"
                          placeholder="请输入更换训练模式的理由"
                          autocomplete="off"></el-input>
              </el-form-item>
            </el-form>
            <el-button type="primary"
                       style="margin-left: 120px"
                       @click="sendModel('changeModel')">确 定</el-button>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

  </div>
</template>

<script>
import request from '@/api/request'
export default {
  name: 'ModifyInfo',
  data() {
    const newPwd1check = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.modifypwd.newPwd) {
        callback(new Error('两次输入新密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      disableModel3: false,
      modifyInfo: false,
      activeName: 'first',
      studentInfo: {
        studentName: '',
        studentId: '',
        schoolName: '',
        major: '',
        level: 0,
        email: '',
      },
      infoForm: {
        studentName: '',
        studentId: '',
        schoolName: '',
        major: '',
        level: '',
        email: '',
      },
      modifypwd: {
        oldPwd: '',
        newPwd: '',
        newPwd1: '',
      },
      changeModel: {
        newModel: '',
        reason: '',
      },
      rules1: {
        oldPwd: [
          { required: true, message: '请输入原始密码', trigger: 'blur' },
        ],
        newPwd: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
        newPwd1: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { validator: newPwd1check, trigger: 'blur' },
        ],
      },
      rules2: {
        newModel: [
          { required: true, message: '请选择要更换的模式', trigger: 'change' },
        ],
        reason: [
          { required: true, message: '请输入输入更换的理由', trigger: 'blur' },
        ],
      },
    }
  },
  methods: {
    submitForm() {
      request({
        url: '/student/message',
        method: 'POST',
        data: {
          userId: sessionStorage.getItem('token'),
          name: this.infoForm.studentName,
          account: this.infoForm.studentId,
          college: this.infoForm.schoolName,
          major: this.infoForm.major,
          userLevel: this.infoForm.level,
          email: this.infoForm.email,
        },
      }).then((res) => {
        // console.log(res);
        if (res.code == 200) {
          this.$message({
            message: '修改成功',
            type: 'success',
          })
          // this.$router.push("/trainHome/modifyInfo");
        } else {
          this.$message({
            message: '修改失败',
            type: 'success',
          })
        }
        this.getInfo()
        this.modifyInfo = false
      })
    },
    //提交修改密码申请
    sendPwd(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request({
            url: '/modifyPwd ',
            method: 'POST',
            data: {
              userId: sessionStorage.getItem('token'),
              oldPwd: this.modifypwd.oldPwd,
              newPwd: this.modifypwd.newPwd,
            },
          }).then((res) => {
            this.$message(res.msg)
            this.$refs[formName].resetFields()
          })
        }
      })
    },

    //提交更换模式的请求
    sendModel(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request({
            url: '/modifyModel',
            method: 'POST',
            data: {
              userId: sessionStorage.getItem('token'),
              userType: 1,
              reason:
                '我想要申请更换到' +
                this.changeModel.newModel +
                ';申请理由为：' +
                this.changeModel.reason,
            },
          }).then((res) => {
            this.$message(res.msg)
            this.$refs[formName].resetFields()
          })
        } else {
          return false
        }
      })
    },
    //触发标签页
    handleClick() {
      if (this.activeName == 'second') {
        this.$refs['modifyPwd'].resetFields()
      }
      if (this.activeName == 'third') {
        this.$refs['changeModel'].resetFields()
      }
    },
    getInfo() {
      request({
        url: '/getInfo',
        method: 'POST',
        data: {
          userId: sessionStorage.getItem('token'),
        },
      }).then((res) => {
        this.infoForm.studentName = res.data.name
        this.infoForm.studentId = res.data.account
        this.infoForm.schoolName = res.data.college
        this.infoForm.major = res.data.major
        this.infoForm.level = res.data.userLevel
        this.infoForm.email = res.data.email
        this.studentInfo.studentName = res.data.name
        this.studentInfo.studentId = res.data.account
        this.studentInfo.schoolName = res.data.college
        this.studentInfo.major = res.data.major
        this.studentInfo.level = res.data.userLevel
        this.studentInfo.email = res.data.email
      })
    },

    //查看这个人是否有资格开启模式3
    checkIfQualifiedChooseModel3() {
      request({
        url: '/getUserNormalTest',
        method: 'post',
        data: {
          userId: sessionStorage.getItem('token'),
        },
      }).then((res) => {
        let data = res.data
        if (data > 0) {
          this.disableModel3 = false
        }

        if (data == 0) {
          this.disableModel3 = true
          this.$message({
            type: 'info',
            message: '完成模式二的试卷方可申请模式三',
          })
        }
      })
    },
  },

  mounted() {
    this.checkIfQualifiedChooseModel3()
  },

  created() {
    this.getInfo()
  },
}
</script>

<style scoped>
.info-form {
  width: 500px;
  margin: 50px;
}
</style>
