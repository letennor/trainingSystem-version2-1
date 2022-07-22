<template>
  <div>

    <el-row>

      <el-col :span="20">

        <el-input type="textarea"
                  :rows="8"
                  placeholder="请输入题目"
                  v-model="questionTitle"
                  style="font-size:20px">
        </el-input>
      </el-col>

      <el-col :span="20"
              style="margin-top:50px">
        <el-input type="textarea"
                  :rows="5"
                  placeholder="请输入答案（若有多个空请以“；”隔开）"
                  v-model="questionAnswer"
                  style="font-size:20px">
        </el-input>
      </el-col>

      <el-col :span="24">
        <el-button type="success"
                   plain
                   style="margin-left:450px; margin-top: 50px; width: 200px; height:50px; font-size: 20px"
                   @click="uploadQuestion">完成创建</el-button>

      </el-col>

    </el-row>

  </div>
</template>
<script>
import request from '@/api/request'

export default {
  name: 'InputQuestionInfoType1',
  data() {
    return {
      questionTitle: null,
      questionAnswer: null,
    }
  },

  methods: {
    beforeUpload(file) {
      console.log(file)
    },
    uploadQuestion() {
      //上传题目
      request({
        url: '/uploadQuestion',
        method: 'post',
        data: {
          questionTitle: this.questionTitle,
          questionAnswer: this.questionAnswer,
          questionType: this.$route.params.questionType,
          questionLevel: this.$route.params.questionLevel,
          kowledgePoint: this.$route.params.selectKnowledgePoint,
          questionForm: this.$route.params.questionForm,
          teacherId: sessionStorage.getItem('token'),
        },
      })

      this.$message({
        type: 'success',
        message: '上传成功！',
      })

      this.$router.push({
        name: 'UploadQuestion',
      })
    },
  },

  mounted() {
    console.log(this.$route.params.questionType)
    console.log(this.$route.params.questionLevel)
    console.log(this.$route.params.selectKnowledgePoint)
    console.log(this.$route.params.questionForm)
  },
}
</script>
<style>
</style>