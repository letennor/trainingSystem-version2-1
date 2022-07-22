<template>
  <div>

    <el-row>

      <span style="font-size: 30px"><strong>专项试卷</strong></span>
      <el-button type="primary"
                 style="float: right; width: 200px"
                 @click="back">
        返回
      </el-button>
    </el-row>

    <el-row>
      <el-card shadow="hover"
               style="margin-top:10px"
               v-for="specialTest in specialTestList"
               :key="specialTest.specialId"
               @click.native="clickSpecialTest(specialTest)">
        <el-descriptions>
          <el-descriptions-item label="试卷名">{{specialTest.testName}}</el-descriptions-item>
          <el-descriptions-item label="知识点">{{specialTest.knowledgePoint}}</el-descriptions-item>
          <el-descriptions-item label="难度">{{specialTest.specialTestLevel}}</el-descriptions-item>
          <el-descriptions-item label="及格线">{{specialTest.specialThreshold}}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{renderTime(specialTest.gmtCreate)}}</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </el-row>

    <el-dialog :visible.sync="isClickSpecialTest">

      <el-row>

        <el-col :span="8">
          <el-button type="danger"
                     style="width: 150px"
                     @click="deleteSpecialTest">删除</el-button>
        </el-col>

        <el-col :span="8">
          <el-button type="primary"
                     style="width: 150px"
                     @click="modifySpecialTest">修改</el-button>
        </el-col>

        <el-col :span="8">
          <el-button type="primary"
                     style="width: 150px"
                     @click="getDetail">查看详情</el-button>
        </el-col>

      </el-row>

    </el-dialog>
    <el-backtop :bottom="60"><i class="el-icon-caret-top"></i></el-backtop>
  </div>
</template>
<script>
import request from '../../../api/request'

export default {
  name: 'SpecialTestManagement',

  data() {
    return {
      specialTestList: [],
      clickedSpecialTest: null,
      isClickSpecialTest: false,
      isChangable: false,
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

    getTeacherSpecialTest() {
      request({
        url: '/getTeacherSpecialTest',
        method: 'post',
        data: {
          teacherId: sessionStorage.getItem('token'),
        },
      }).then((res) => {
        console.log(res)
        this.specialTestList = res.data
      })
    },

    clickSpecialTest(specialTest) {
      console.log(specialTest)
      this.isClickSpecialTest = true
      this.clickedSpecialTest = specialTest

      //判断专项能否被修改
      request({
        url: '/isSpecialTestChangable',
        method: 'post',
        data: {
          specialId: specialTest.specialId,
        },
      }).then((res) => {
        let code = res.code

        if (code === 200) {
          this.isChangable = true
        } else {
          this.isChangable = false
        }
      })
    },

    deleteSpecialTest() {
      if (this.isChangable) {
        if (confirm('是否要删除？') == true) {
          //发送请求删除
          request({
            url: '/deleteOneSpecialTest',
            method: 'post',
            data: {
              specialId: this.clickedSpecialTest.specialId,
            },
          }).then((res) => {
            console.log(res)
            this.$message({
              type: 'success',
              message: '删除成功！',
            })

            this.getTeacherSpecialTest()
            this.isClickSpecialTest = false
          })
        }
      } else {
        this.$message({
          type: 'error',
          message: '该试题已被列入训练计划，无法删除',
        })
      }
    },

    modifySpecialTest() {
      if (this.isChangable) {
        //跳转到修改页面
        this.$router.push({
          name: 'ModifySpecialTest',
          params: {
            specialTest: this.clickedSpecialTest,
          },
        })
      } else {
        this.$message({
          type: 'error',
          message: '该试题已被列入训练计划，无法修改',
        })
      }
    },

    getDetail() {
      //去到QuestionManagement
      this.$router.push({
        name: 'QuestionManagement',
        params: {
          from: 'SpecialTestManagement',
          specialId: this.clickedSpecialTest.specialId,
        },
      })
    },

    back() {
      this.$router.push({
        name: 'SourceManagement',
      })
    },
  },

  mounted() {
    this.getTeacherSpecialTest()
  },
}
</script>
<style scoped>
</style>