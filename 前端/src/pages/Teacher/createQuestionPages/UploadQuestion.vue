<template>
  <el-row :gutter="40"
          style="height: 700px">
    <el-col :span="12">
      <el-card shadow="hover"
               @click.native="uploadQuestion(1)"
               style="font-size:30px;">
        制作填空题
      </el-card>
    </el-col>

    <el-col :span="12">
      <el-card shadow="hover"
               @click.native="uploadQuestion(2)"
               style="font-size:30px">
        制作编程题
      </el-card>
    </el-col>
  </el-row>
</template>
<script>
export default {
  name: 'UploadQuestion',
  data() {
    return {
      questionForm: null,
    }
  },

  methods: {
    uploadQuestion(value) {
      this.questionForm = value

      this.$router.push({
        name: 'SelectQuestionBasicInfo',
        params: {
          questionForm: this.questionForm,
        },
      })
    },
  },
}
</script>
<style scoped>
.el-card {
  height: 300px;
  text-align: center;
  line-height: 280px;
}
</style>