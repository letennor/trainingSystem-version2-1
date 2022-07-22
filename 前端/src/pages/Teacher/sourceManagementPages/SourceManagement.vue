<template>
  <div style=" width: 1000px;
  height: 700px;
  position: relative;
  margin: auto;">
    <el-row>
      <el-card shadow="hover"
               @click.native="questionManagement">
        <i class="el-icon-edit"
           style="position: absolute; left: 300px; top: 50px"></i>题目管理
      </el-card>
    </el-row>

    <el-row>
      <el-card shadow="hover"
               @click.native="specialTestManagement">
        <i class="el-icon-tickets"
           style="position: absolute; left: 300px; top: 50px"></i>专项试卷管理
      </el-card>
    </el-row>

    <el-row>
      <el-card shadow="hover"
               @click.native="testSetManagement">
        <i class="el-icon-document-copy"
           style="position: absolute; left: 300px; top: 50px"></i>套题管理
      </el-card>
    </el-row>
  </div>
</template>
<script>
export default {
  name: 'SourceManagement',

  methods: {
    questionManagement() {
      //跳转到题目管理页面
      this.$router.push({
        name: 'QuestionManagement',
        params: {
          from: 'SourceManagement',
        },
      })
    },

    specialTestManagement() {
      this.$router.push({
        name: 'SpecialTestManagement',
      })
    },

    testSetManagement() {
      this.$router.push({
        name: 'TestSetManagement',
      })
    },
  },
}
</script>

<style scoped>
.el-row {
  height: 200px;
}

.el-card {
  height: 150px;
  text-align: center;
  font-size: 50px;
  line-height: 100px;
}
</style>