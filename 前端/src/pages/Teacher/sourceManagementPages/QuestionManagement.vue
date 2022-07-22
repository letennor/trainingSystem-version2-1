<template>
  <div>
    <el-row v-if="$route.params.from === 'SourceManagement'">

      <span style="font-size: 30px"><strong>我的题目</strong></span>
      <el-button type="primary"
                 style="float: right; width: 200px"
                 @click="backSourceManagement">
        返回
      </el-button>
    </el-row>

    <el-row v-if="$route.params.from === 'SpecialTestManagement'">

      <span style="font-size: 30px"><strong>专项试卷题目</strong></span>
      <el-button type="primary"
                 style="float: right; width: 200px"
                 @click="backSpecialTestManagement">
        返回
      </el-button>
    </el-row>

    <el-row>

      <el-row v-if="$route.params.from === 'NormalTestManagement'">

        <span style="font-size: 30px"><strong>普通试卷题目</strong></span>
        <el-button type="primary"
                   style="float: right; width: 200px"
                   @click="backNormalTestManagement">
          返回
        </el-button>
      </el-row>
    </el-row>

    <el-row>

      <el-col v-for="question in questionList"
              :key="question.questionId">
        <el-card shadow="hover"
                 style="margin-top: 10px"
                 @click.native="clickQuestion(question)">

          {{question.questionTitle}}
        </el-card>

      </el-col>

      <el-dialog :visible.sync="isClickQuestion">

        <el-row>

          <el-col :span="8">
            <el-button type="danger"
                       style="width: 150px"
                       @click="deleteQuestion">删除</el-button>
          </el-col>

          <el-col :span="8">
            <el-button type="primary"
                       style="width: 150px"
                       @click="modifyQuestion">修改</el-button>
          </el-col>

          <el-col :span="8">
            <el-button type="primary"
                       style="width: 150px"
                       @click="getDetail">查看详情</el-button>
          </el-col>

        </el-row>

      </el-dialog>

    </el-row>
    <el-backtop :bottom="60"><i class="el-icon-caret-top"></i></el-backtop>
  </div>
</template>
<script>
import request from '../../../api/request'
export default {
  name: 'QuestionManagement',

  data() {
    return {
      clickedQuestion: null,
      questionList: [],
      isClickQuestion: false,
      isClickModifyType1: false,
      isClickModifyType2: false,
      isChangable: null,

      questionBasicInfo: null,
      questionIOList: [],
      questionAnswerList: [],
    }
  },

  methods: {
    //获得该老师的所有题目
    getMyQuestion() {
      request({
        url: 'getQuestionByTeacherId',
        method: 'post',
        data: {
          teacherId: sessionStorage.getItem('token'),
        },
      }).then((res) => {
        console.log('questionList', res.data)
        this.questionList = res.data
      })
    },

    //获得传过来的专项的试卷题目
    getSpecialTestQuestion() {
      request({
        url: '/getSpecialTestQuestion',
        method: 'post',
        data: {
          specialId: this.$route.params.specialId,
        },
      }).then((res) => {
        console.log('specialTestQuestion', res)
        this.questionList = res.data
      })
    },

    //获得传过来的普通试卷的题目
    getNormalTestQuestion() {
      request({
        url: '/getNormalTestQuestion',
        method: 'post',
        data: {
          normalId: this.$route.params.normalId,
        },
      }).then((res) => {
        console.log('normalQuestion', res)
        this.questionList = res.data
      })
    },

    clickQuestion(question) {
      this.clickedQuestion = question
      this.isClickQuestion = true

      //发送请求
      request({
        url: '/checkIsChangable',
        method: 'post',
        data: {
          questionId: question.questionId,
          test: 1,
        },
      }).then((res) => {
        let code = res.code
        if (code === 200) {
          this.isChangable = true
        } else {
          //不可修改
          this.isChangable = false
        }
      })

      //发送请求，请求这个题目的所有数据
      request({
        url: '/questionDetails',
        method: 'post',

        data: {
          questionId: question.questionId,
        },
      }).then((res) => {
        //   console.log('要修改的题目', res)
        this.questionBasicInfo = res.data.questionBasicInfo
        this.questionIOList = res.data.questionIOList
        this.questionAnswerList = res.data.questionAnswerList
        console.log('questionBasicInfo', this.questionBasicInfo)
        console.log('questionIOList', this.questionIOList)
        console.log('questionAnswerList', this.questionAnswerList)
      })
    },

    deleteQuestion() {
      let question = this.clickedQuestion

      if (this.isChangable) {
        if (confirm('是否删除确认删除') == true) {
          //发送请求
          request({
            url: '/deleteOneQuestion',
            method: 'post',
            data: {
              questionId: question.questionId,
            },
          }).then(() => {
            //删除成功
            this.$message({
              type: 'success',
              message: '删除成功！',
            })

            // 重新刷新
            this.getMyQuestion()
            //退出dialog
            this.isClickQuestion = false
          })
        }
      } else {
        this.$message({
          type: 'error',
          message: '题目已被收录，不可删除',
        })
      }
    },

    modifyQuestion() {
      let question = this.clickedQuestion

      console.log('点击了修改', question)

      if (this.isChangable) {
        //跳转
        if (this.questionBasicInfo.questionForm === 1) {
          this.$router.push({
            name: 'ModifyQuestionType1',
            params: {
              questionBasicInfo: this.questionBasicInfo,
              questionIOList: this.questionIOList,
              questionAnswerList: this.questionAnswerList,
            },
          })
        } else {
          this.$router.push({
            name: 'ModifyQuestionType2',
            params: {
              questionBasicInfo: this.questionBasicInfo,
              questionIOList: this.questionIOList,
              questionAnswerList: this.questionAnswerList,
            },
          })
        }
      } else {
        this.$message({
          type: 'error',
          message: '题目已被收录，不可修改',
        })
      }
    },

    getDetail() {
      let question = this.clickedQuestion

      this.$router.push({
        name: 'QuestionDetail',
        params: {
          question: question,
          from: this.$route.params.from,
          specialId: this.$route.params.specialId,
          normalId: this.$route.params.normalId,
          setId: this.$route.params.setId,
        },
      })
    },

    backSpecialTestManagement() {
      //返回到SpecialTestManagement
      this.$router.push({
        name: 'SpecialTestManagement',
      })
    },

    backNormalTestManagement() {
      this.$router.push({
        name: 'NormalTestManagement',
        params: {
          setId: this.$route.params.setId,
        },
      })
    },

    backSourceManagement() {
      this.$router.push({
        name: 'SourceManagement',
      })
    },
  },

  mounted() {
    if (this.$route.params.from === 'SourceManagement') {
      this.getMyQuestion()
    }

    if (this.$route.params.from === 'SpecialTestManagement') {
      this.getSpecialTestQuestion()
      console.log('specialId', this.$route.params.specialId)
    }

    if (this.$route.params.from === 'NormalTestManagement') {
      this.getNormalTestQuestion()
      console.log('normalId', this.$route.params.normalId)
    }
  },
}
</script>

<style scoped>
* {
  white-space: pre-line;
}
</style>