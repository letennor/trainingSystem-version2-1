<template>
  <div>
    <el-row :gutter="40"
            style="margin-top: 20px; margin: auto; width: 500px">
      <el-col :span="24">
        <el-input placeholder="请输入内容"
                  v-model="specialTestName">
          <template slot="prepend">试卷名称</template>
        </el-input>
      </el-col>

      <el-col :span="24"
              style="margin-top: 10px">

        <span>题目数量：</span>
        <template>
          <el-input-number v-model="questionNumber"
                           :min="4"
                           :max="10"
                           placeholder="题目数量"></el-input-number>
        </template>
      </el-col>

      <el-col :span="24"
              style="margin-top: 10px">
        <span>及格线： &nbsp;&nbsp;&nbsp;</span>
        <template>
          <el-input-number v-model="threshold"
                           :min="0"
                           :max="questionNumber"
                           placeholder="及格线"></el-input-number>
        </template>
      </el-col>

      <el-col :span="24"
              style="margin-top:10px">
        <span>试卷难度：</span>
        <el-rate v-model="specialLevel"
                 show-text
                 :texts="texts"
                 :max="3">
        </el-rate>
      </el-col>

      <el-col :span="24"
              style="margin-top: 30px">
        <el-button type="primary"
                   style="float:right"
                   @click="submitCreateSpecialTest">创建试卷</el-button>
      </el-col>

    </el-row>

  </div>
</template>
<script>
export default {
  data() {
    return {
      specialTestName: '',
      threshold: null,
      texts: ['简单', '普通', '困难'],
      specialLevel: null,
      drawer: false,
      questionNumber: null,
    }
  },
  methods: {
    submitCreateSpecialTest() {
      this.$router.push({
        name: 'SelectQuestion',
        params: {
          //需要发送过去的信息：testName，threshold，specialLevel, questionNumber
          specialLevel: this.specialLevel,
          questionNumber: this.questionNumber,
          testName: this.specialTestName,
          specialThreshold: this.threshold,
          testType: 0,
        },
      })
    },
  },
}
</script>


<style scoped>
.el-rate {
  font-size: 40px;
  margin: 0;
  height: 0px;
}

.el-row {
  margin-bottom: 30px;
}

.el-radio-button {
  margin-bottom: 20px;
  margin-left: 30px;
  margin-top: 10px;
}
</style>
