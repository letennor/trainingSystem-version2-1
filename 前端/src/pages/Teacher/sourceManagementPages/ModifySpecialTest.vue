<template>
  <div style="  width: 600px;
  height: 500px;
  position: relative;
  margin: auto;">
    <el-row>
      <el-col :span="24">
        <el-input placeholder="请输入内容"
                  v-model="testName">
          <template slot="prepend">试卷名字</template>
        </el-input>
      </el-col>

      <el-col :span="24">
        <el-input-number v-model="specialThreshold"
                         :min="0"
                         :max="questionNumber"
                         placeholder="及格线"></el-input-number>

      </el-col>

      <el-col :span="24">
        <el-input placeholder="难度"
                  v-model="specialLevel"
                  :disabled="true">
          <template slot="prepend">难度</template>
        </el-input>
      </el-col>

      <el-col :span="24">
        <el-input placeholder="知识点"
                  v-model="knowledgePoint"
                  :disabled="true">
          <template slot="prepend">知识点</template>
        </el-input>
      </el-col>

      <el-col :span="24">
        <el-input placeholder="创建时间"
                  v-model="createTime"
                  :disabled="true">
          <template slot="prepend">创建时间</template>
        </el-input>
      </el-col>

      <el-col :span="24">
        <el-button type="success"
                   style="float: left; margin-top: 20px; width: 150px; height:50px; font-size: 20px"
                   @click="submitModify">提交修改</el-button>

        <el-button type="primary"
                   style="float: right; margin-top: 20px; width: 150px; height:50px; font-size: 20px"
                   @click="back">返回</el-button>
      </el-col>

    </el-row>
  </div>
</template>
<script>
import request from '../../../api/request'

export default {
  name: 'ModifySpecialTest',
  data() {
    return {
      specialTest: null,
      specialThreshold: null,
      testName: null,
      specialLevel: null,
      knowledgePoint: null,
      createTime: null,
      questionNumber: null,
    }
  },

  methods: {
    back() {
      this.$router.push({
        name: 'SpecialTestManagement',
      })
    },
    renderTime(date) {
      var dateee = new Date(date).toJSON()
      return new Date(+new Date(dateee) + 8 * 3600 * 1000)
        .toISOString()
        .replace(/T/g, ' ')
        .replace(/\.[\d]{3}Z/, '')
    },

    submitModify() {
      //发送请求
      request({
        url: '/modifySpecialTest',
        method: 'post',
        data: {
          specialId: this.specialTest.specialId,
          testName: this.testName,
          specialTestThreshold: this.specialThreshold,
        },
      }).then((res) => {
        console.log(res)

        this.$message({
          type: 'success',
          message: '修改成功！',
        })

        this.$router.push({
          name: 'SpecialTestManagement',
        })
      })
    },
  },

  mounted() {
    this.specialTest = this.$route.params.specialTest
    this.testName = this.specialTest.testName
    this.specialLevel = this.specialTest.specialTestLevel
    this.knowledgePoint = this.specialTest.knowledgePoint
    this.createTime = this.renderTime(this.specialTest.gmtCreate)
    this.questionNumber = this.specialTest.questionNumber
    this.specialThreshold = this.specialTest.specialThreshold
  },
}
</script>

<style scoped>
.el-col {
  margin-top: 10px;
}
</style>