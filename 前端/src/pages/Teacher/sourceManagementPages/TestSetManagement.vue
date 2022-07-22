<template>
  <div>

    <el-row>

      <span style="font-size: 30px"><strong>套题试卷</strong></span>
      <el-button type="primary"
                 style="float: right; width: 200px"
                 @click="back">
        返回
      </el-button>
    </el-row>

    <el-row>
      <el-card shadow="hover"
               style="margin-top:10px"
               v-for="testSet in testSetList"
               :key="testSet.setId"
               @click.native="clicktestSet(testSet)">
        <el-descriptions>
          <el-descriptions-item label="套题Id">{{testSet.setId}}</el-descriptions-item>
          <el-descriptions-item label="难度">{{testSet.setLevel}}</el-descriptions-item>
          <el-descriptions-item label="及格线">{{testSet.threshold}}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{renderTime(testSet.gmtCreate)}}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-row>

    <el-dialog :visible.sync="isClickTestSet">

      <el-row>

        <el-col :span="8">
          <el-button type="danger"
                     style="width: 150px"
                     @click="deleteTestSet">删除</el-button>
        </el-col>

        <el-col :span="8">
          <el-button type="primary"
                     style="width: 150px"
                     @click="modifyTestSet">修改及格线</el-button>
        </el-col>

        <el-col :span="8">
          <el-button type="primary"
                     style="width: 150px"
                     @click="getDetail">查看试卷详情</el-button>
        </el-col>

      </el-row>

    </el-dialog>

    <el-dialog title="修改及格线"
               :visible.sync="isClickModify">

      <el-row>
        <el-col :span="10"
                style="margin-left: 50px">
          <el-input-number v-model="newThreshold"
                           :min="0"
                           :max="10"
                           placeholder="及格线"></el-input-number>
        </el-col>

        <el-col :span="10">
          <el-button type="success"
                     plain
                     @click="submitModify"
                     style="margin-left: 50px; width: 200px">
            确认修改
          </el-button>
        </el-col>

      </el-row>

    </el-dialog>
    <el-backtop :bottom="60"><i class="el-icon-caret-top"></i></el-backtop>
  </div>
</template>
<script>
import request from '../../../api/request'

export default {
  name: 'TestSetManagement',

  data() {
    return {
      testSetList: [],
      clickedTestSet: null,
      isChangable: false,
      isClickTestSet: false,
      isClickModify: false,
      newThreshold: null,
    }
  },

  methods: {
    //取得老师所有套题
    getTeacherTestSet() {
      request({
        url: '/getTeacherTestSet',
        method: 'post',
        data: {
          teacherId: sessionStorage.getItem('token'),
        },
      }).then((res) => {
        console.log(res)
        this.testSetList = res.data
      })
    },

    clicktestSet(testSet) {
      this.clickedTestSet = testSet
      this.newThreshold = this.clickedTestSet.threshold
      this.isClickTestSet = true

      //判断套题是否可修改
      request({
        url: '/isTestSetChangable',
        method: 'post',
        data: {
          setId: testSet.setId,
        },
      }).then((res) => {
        let code = res.code
        if (code === 200) {
          //可修改
          this.isChangable = true
        } else {
          this.isChangable = false
        }
      })
    },

    deleteTestSet() {
      if (this.isChangable) {
        //发送请求删除
        if (confirm('是否要删除？') == true) {
          request({
            url: '/deleteTestSet',
            method: 'post',
            data: {
              setId: this.clickedTestSet.setId,
            },
          }).then((res) => {
            console.log(res)
            this.$message({
              type: 'success',
              message: '删除成功！',
            })

            //刷新
            this.getTeacherTestSet()
            this.isClickTestSet = false
          })
        }
      } else {
        this.$message({
          type: 'error',
          message: '该套题已被列入学习计划，不可删除',
        })
      }
    },

    modifyTestSet() {
      if (this.isChangable) {
        this.isClickModify = true
        this.isClickTestSet = false
      } else {
        this.$message({
          type: 'error',
          message: '该套题已被列入学习计划，不可修改',
        })
      }
    },

    getDetail() {
      //跳转至试卷页面
      this.$router.push({
        name: 'NormalTestManagement',
        params: {
          setId: this.clickedTestSet.setId,
          isChangable: this.isChangable,
        },
      })
    },

    submitModify() {
      //发送请求修改
      request({
        url: '/modifyThreshold',
        method: 'post',
        data: {
          setId: this.clickedTestSet.setId,
          threshold: this.newThreshold,
        },
      }).then((res) => {
        console.log(res)

        this.$message({
          type: 'success',
          message: '修改成功！',
        })

        //刷新
        this.getTeacherTestSet()
        this.isClickModify = false
      })
    },

    renderTime(date) {
      var dateee = new Date(date).toJSON()
      return new Date(+new Date(dateee) + 8 * 3600 * 1000)
        .toISOString()
        .replace(/T/g, ' ')
        .replace(/\.[\d]{3}Z/, '')
    },

    back() {
      this.$router.push({
        name: 'SourceManagement',
      })
    },
  },

  mounted() {
    this.getTeacherTestSet()
  },
}
</script>


<style scoped>
</style>


