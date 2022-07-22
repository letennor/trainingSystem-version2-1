<template>
  <div>
    <!-- 导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>训练系统</el-breadcrumb-item>
      <el-breadcrumb-item>开始训练</el-breadcrumb-item>
    </el-breadcrumb>

    <el-row style="margin: 50px 0px 0px 0px;">
      <!-- 欢迎，你上一次做的是xxx，这次应该做xxx啦，开始 -->

      <el-col :span="24">
        <el-collapse-transition>
          <div v-show="showDetail">
            <div class="transition-box"
                 style="width: 100%;font-size: 20px"
                 type="button"
                 @click="startTraining">{{nextTrainingInfo}}</div>
          </div>
        </el-collapse-transition>
      </el-col>
      <el-col :span="19" :offset="5">
        <div class="blockboard">
<!--      <span style="display: inline-block;font-size: 30px; color: red; margin-top: 150px">要坚持训练哦！</span>-->
      </div>
      </el-col>
    </el-row>
    <!-- <div style="display: flex"> -->
      <!-- <div class="blockboard">
      <span style="display: inline-block;font-size: 30px; color: red; margin-top: 150px">要坚持训练哦</span>
      </div> -->
      <!-- <div style="">
      <img style="width: 100%;" src="@/assets/images/t1.jpeg" alt="">

      </div> -->
    <!-- </div> -->

    <!-- 第一次进入的时候，水平测试欢迎 -->
    <el-dialog title="欢迎"
               :visible="dialogVisible"
               width="60%">
      <div class="block"
           style="margin-bottom: 50px">

        <el-steps :active="active"
                  align-center
                  finish-status="success"
                  style="font-size: 30px">
          <el-step title="欢迎使用ITS4CSC"></el-step>
          <el-step title="为了便于我们了解你的水平，先做一次水平测试吧~"></el-step>
          <el-step title="进入水平测试"></el-step>
        </el-steps>

        <el-button style="margin-top: 30px; float: right"
                   type="primary"
                   @click="next">{{buttonContent}}</el-button>

        <el-button style="margin-top: 30px; float: left"
                   type="danger"
                   @click="leave">离开</el-button>
      </div>

    </el-dialog>

    <!--完成了一套练习 启动model3 -->
    <el-dialog title="恭喜"
               :visible="model3Active"
               width="60%">
      <span><strong>恭喜您完成了一套套题练习，根据您的数据，我们发现您对一些知识点掌握程度不够，接下来的训练，我们将会对您薄弱的知识点进行加强练习。</strong></span>
      <span slot="footer"
            class="dialog-footer">
        <el-button type="primary"
                   @click="confirmModel3Active">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 展示系统生成的specialTest -->
    <el-dialog title="接下来的练习"
               :visible="model3Show"
               width="60%">

      <el-descriptions :column="1"
                       border>

        <el-descriptions-item v-for="specialTest in systemGenerateSpecialTest"
                              :key="specialTest.specialId">
          <template slot="label">
            试卷名称
          </template>
          {{specialTest.testName}}
        </el-descriptions-item>

      </el-descriptions>

      <span slot="footer"
            class="dialog-footer">
        <el-button type="primary"
                   @click="confirmStartModel3">开始吧</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import request from '../../../api/request'

export default {
  name: 'Training',
  data() {
    return {
      dialogVisible: false, // 用于控制对话框（当等级为0时，点击模式时弹出）
      level: sessionStorage.getItem('level'),
      active: 0,
      buttonContent: '下一步',
      showDetail: false,
      nextTrainingInfo: '',
      model3Active: false,
      model3Show: false,
      systemGenerateSpecialTest: [],
    }
  },
  methods: {
    toModel1() {
      this.$router.push('/trainModel1')
    },
    toModel2() {
      this.$router.push('/trainModel2?Q=0')
    },
    toModel3() {
      this.$router.push('/trainModel2?Q=0')
    },
    // 进入水平测试
    toLevelTest() {
      this.$router.push('/levelTest')
    },

    next() {
      this.active++

      if (this.active <= 1) {
        this.buttonContent = '下一步'
      }

      if (this.active == 2) {
        this.buttonContent = '进入水平测试'
      }

      if (this.active == 3) {
        //进入水平测试
        this.toLevelTest()
      }
    },

    leave() {
      this.dialogVisible = false
    },

    init() {
      this.dialogVisible = this.level == 0 ? true : false
    },

    startTraining() {
      //根据这个人的level，去到不同的model
      if (this.level == 2 || this.level == 3) {
        this.toModel1()
      }

      if (this.level == 4) {
        this.toModel2()
      }

      if (this.level == 5) {
        this.toModel3()
      }
    },

    //发起请求，看看这个人上一次做的什么试卷，这次应该做什么试卷
    getNextTrainingInfo() {
      request({
        url: '/getNextTrainingInfo',
        method: 'post',
        data: {
          userId: sessionStorage.getItem('token'),
          userLevel: this.level,
        },
      }).then((res) => {
        console.log(res)
        let data = res.data
        let flag = data.flag
        if (flag == 100) {
          this.nextTrainingInfo = '开始学习知识点:' + data.knowledgePointName
        }

        if (flag == 200) {
          this.nextTrainingInfo =
            '您上次没有通过知识点的学习，这次继续吧，请完成试卷：' +
            data.testName
        }

        if (flag == 300) {
          this.nextTrainingInfo =
            '开始套题的训练吧，这次做试卷: 【' +
            data.testName +
            '】' +
            ',包括知识点：'
          for (let i = 0; i < data.knowlegePointListInNormalTest.length; i++) {
            this.nextTrainingInfo =
              this.nextTrainingInfo +
              data.knowlegePointListInNormalTest[i] +
              ' '
          }
        }

        if (flag == 400) {
          this.nextTrainingInfo =
            '这次做的是专项练习:【' + data.testName + '】，开始巩固知识点吧'
        }

        if (flag == 500) {
          this.nextTrainingInfo =
            '这次做的是专项练习:【' + data.testName + '】，继续精进吧'
        }

        if (flag == 600) {
          //将这个人转换为模式2
          this.level = 4
          sessionStorage.setItem('level', 4)
          this.nextTrainingInfo = data.msg
        }
      })
    },

    confirmModel3Active() {
      //点击了介绍模式3的弹窗，此时向系统请求这个人所有的test，这时候所有的test都是系统刚刚生成的专项
      request({
        url: '/getStudentTestByUserId',
        method: 'post',
        data: {
          userId: sessionStorage.getItem('token'),
        },
      }).then((res) => {
        // console.log('刚刚系统生成的专项', res)
        this.systemGenerateSpecialTest = res.data
        console.log('刚刚生成的专项', this.systemGenerateSpecialTest)
        //弹出展示模式3的专项的dialog
        this.model3Show = true
      })

      this.model3Active = false
    },

    confirmStartModel3() {
      //点击“开始吧”
      this.model3Show = false
    },

    ifShowModel3() {
      let status = this.$route.params.status
      if (status == 7 || status == 8) {
        this.model3Active = true
        //将学生的level设置为5
        this.level = 5
        sessionStorage.setItem('level', 5)
      }
    },
  },

  mounted() {
    this.init()
    this.showDetail = true
    //判断是否开启model3Active
    this.ifShowModel3()
    this.getNextTrainingInfo()
  },
}
</script>

<style scoped>
.transition-box {
  margin-bottom: 10px;
  width: 200px;
  height: 100px;
  border-radius: 4px;
  background-color: #409eff;
  text-align: center;
  color: #fff;
  padding: 40px 20px;
  box-sizing: border-box;
  margin-right: 20px;
}
.blockboard {
  background: url(../../../assets/images/blackboard.jpeg);
  width: 700px;
  height: 393px;
  text-align: center;
}
</style>
