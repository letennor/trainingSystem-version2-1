<template>
  <div>

    <el-row>

      <span style="font-size: 30px"><strong>套题试卷管理</strong></span>
      <el-button type="primary"
                 style="float: right; width: 200px"
                 @click="back">
        返回
      </el-button>
    </el-row>

    <el-row>
      <el-card shadow="hover"
               style="margin-top:10px"
               v-for="normalTest in normalTestList"
               :key="normalTest.normalId"
               @click.native="clickNormalTest(normalTest)">
        <el-descriptions>
          <el-descriptions-item label="试卷名">{{normalTest.testName}}</el-descriptions-item>
          <el-descriptions-item label="所属套题">{{normalTest.setId}}</el-descriptions-item>
          <el-descriptions-item label="限时">{{normalTest.limitTime}}</el-descriptions-item>
          <el-descriptions-item label="试题序号">{{normalTest.normalTestNumber}}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{renderTime(normalTest.gmtCreate)}}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-row>

    <el-dialog :visible.sync="isClickNormalTest">

      <el-row>

        <el-col :span="8">
          <el-button type="primary"
                     style="width: 150px"
                     @click="modifyNormalTest">修改</el-button>
        </el-col>

        <el-col :span="8">
          <el-button type="primary"
                     style="width: 150px"
                     @click="getDetail">查看详情</el-button>
        </el-col>

      </el-row>

    </el-dialog>
  </div>
</template>
<script>
import request from '../../../api/request'

export default {
  name: 'NormalTestManagement',

  data() {
    return {
      clickedNormalTest: null,
      normalTestList: [],
      isChangable: false,
      isClickNormalTest: false,
    }
  },

  methods: {
    back() {
      this.$router.push({
        name: 'TestSetManagement',
      })
    },

    renderTime(date) {
      var dateee = new Date(date).toJSON()
      return new Date(+new Date(dateee) + 8 * 3600 * 1000)
        .toISOString()
        .replace(/T/g, ' ')
        .replace(/\.[\d]{3}Z/, '')
    },

    //取得套题的所有试卷
    getAllNormalTest() {
      request({
        url: '/getAllNormalTestBySetId',
        method: 'post',
        data: {
          setId: this.$route.params.setId,
        },
      }).then((res) => {
        console.log(res)
        this.normalTestList = res.data
      })
    },

    clickNormalTest(normalTest) {
      this.clickedNormalTest = normalTest
      this.isClickNormalTest = true
    },

    modifyNormalTest() {
      if (this.isChangable) {
        //跳转到修改框
        this.$router.push({
          name: 'ModifyNormalTest',
          params: {
            normalTest: this.clickedNormalTest,
          },
        })
      } else {
        this.$message({
          type: 'error',
          message: '该试题所属套题已被列入计划，无法修改',
        })
      }
    },

    getDetail() {
      //跳转到QuestionManagement
      this.$router.push({
        name: 'QuestionManagement',
        params: {
          from: 'NormalTestManagement',
          normalId: this.clickedNormalTest.normalId,
          setId: this.$route.params.setId,
        },
      })
    },
  },

  mounted() {
    this.getAllNormalTest()
    this.isChangable = this.$route.params.isChangable
  },
}
</script>

<style scoped>
</style>