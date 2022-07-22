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
        <el-input-number v-model="limitTime"
                         :min="0"
                         :max="180"
                         placeholder="限定时间"></el-input-number>

      </el-col>

      <el-col :span="24">
        <el-input placeholder="所属套题"
                  v-model="setId"
                  :disabled="true">
          <template slot="prepend">所属套题</template>
        </el-input>
      </el-col>

      <el-col :span="24">
        <el-input placeholder="试题序号"
                  v-model="normalTestNumber"
                  :disabled="true">
          <template slot="prepend">试题序号</template>
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
                   plain
                   style="margin-left:200px; margin-top: 20px; width: 200px; height:50px; font-size: 20px"
                   @click="submitModify">提交修改</el-button>

      </el-col>
    </el-row>
  </div>
</template>
<script>
import request from '../../../api/request'

export default {
  name: 'ModifyNormalTest',

  data() {
    return {
      normalTest: null,
      testName: null,
      createTime: null,
      limitTime: null,
      setId: null,
      normalTestNumber: null,
    }
  },

  methods: {
    renderTime(date) {
      var dateee = new Date(date).toJSON()
      return new Date(+new Date(dateee) + 8 * 3600 * 1000)
        .toISOString()
        .replace(/T/g, ' ')
        .replace(/\.[\d]{3}Z/, '')
    },

    submitModify() {
      //修改
      request({
        url: '/modifyNormalTest',
        method: 'post',
        data: {
          normalId: this.normalTest.normalId,
          testName: this.testName,
          limitTime: this.limitTime,
        },
      }).then((res) => {
        console.log(res)
        this.$message({
          type: 'success',
          message: '修改成功！',
        })

        this.$router.push({
          name: 'TestSetManagement',
        })
      })
    },
  },

  mounted() {
    console.log(this.$route.params.normalTest)
    this.normalTest = this.$route.params.normalTest
    this.testName = this.normalTest.testName
    this.createTime = this.renderTime(this.normalTest.gmtCreate)
    this.limitTime = this.normalTest.limitTime
    this.setId = this.normalTest.setId
    this.normalTestNumber = this.normalTest.normalTestNumber
  },
}
</script>
<style scoped>
.el-col {
  margin-top: 10px;
}
</style>