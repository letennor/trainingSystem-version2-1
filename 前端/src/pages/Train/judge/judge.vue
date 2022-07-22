<template>
  <div>
    <div class="header-bar">
      <div class="header-left">
        <div class="question-title">
          <i class="el-icon-s-flag"></i>{{ `${testType == 1 ? "普通训练" : "专项训练"}` }}
        </div>
      </div>
      <div class="header-right">
        <ul class="code-list-box">
          <li>
            <el-button type="text"
                       @click="
                $confirm('退出则此次练习作废，确定退出?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning',
                })
                  .then(() => {
                    $router.push('/trainHome/training');
                  })
                  .catch(() => {})
              ">
              <i class="el-icon-s-home"></i>
              训练主页
            </el-button>
          </li>
        </ul>
        <a href=""
           class="head-pic"> </a>
      </div>
    </div>
    <div class="div-card">
      <el-card class="card">
        <h3 style="text-align: center">请对你的答案进行判断</h3>
        <p>
          请对你的答案进行判断，将结果勾选出来，如不进行勾选，在提交时默认为错。
        </p>
        <p style="text-align: right; font-size: 10px; color: red">
          请勿离开此页面！
        </p>
      </el-card>
    </div>
    <el-row :gutter="20"
            style="margin: 0px">
      <el-col :span="4"
              :offset="2">
        <h3>题目序号</h3>
        <el-row>
          <el-col :span="8"
                  v-for="(question, index) in questions"
                  :key="index"
                  style="margin: 5px 0px">
            <div class="question-index">
              <el-button :type="index === Number($route.query.J) ? 'primary' : ''"
                         circle
                         @click="chooseQuestion(index)"
                         style="width: 100%">{{ index + 1 }}
              </el-button>
            </div>
          </el-col>
        </el-row><br /><br /><br /><br />
        <el-button type="primary"
                   @click="submitJudge">提交</el-button>
      </el-col>
      <el-col :span="16">
        <div>
          <h2 class="subject-item-title">题目描述</h2>
          <div class="subject-describe">
            <div class="subject-question">
              <div>
                {{ questions[$route.query.J].questionTitle }}
              </div>
              <div v-if="questions[$route.query.J].questionImgList.length != 0">
                <div>
                  <img alt=""
                       :src="`${
                      questions[$route.query.J].questionImgList[0]
                        .questionImgUrl
                    }`"
                       style="height: auto; width: 120px" /><br />
                </div>
              </div>
            </div>
            <el-collapse>
              <el-collapse-item v-for="(questionIO, index) in questions[$route.query.J]
                  .questionIOList"
                                :key="index"
                                :title="'示例' + (index + 1)">
                <div class="question-oi-bd">
                  <div class="question-oi-mod">
                    <h2>输入</h2>
                    <div class="question-oi-cont">
                      <pre>{{ questionIO.input }}</pre>
                    </div>
                  </div>
                  <div class="question-oi-mod">
                    <h2>输出</h2>
                    <div class="question-oi-cont">
                      <pre>{{ questionIO.output }}</pre>
                    </div>
                  </div>
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>
          <el-row :gutter="10">
            <el-col :span="12">
              <div style="height: 90px">
                <h3>参考思路</h3>
                <el-radio-group v-model="radio">
                  <el-radio-button v-for="(answer, index) in questions[$route.query.J]
                      .questionAnswerList"
                                   :key="index"
                                   :label="index">{{
                      answer.language == 1
                        ? "C"
                        : answer.language == 2
                        ? "Java"
                        : "Python"
                    }}</el-radio-button>
                </el-radio-group>
              </div>
              <pre>{{
                  radio == 0
                    ? questions[$route.query.J].questionAnswerList[0]
                        .standardAnswer
                    : questions[$route.query.J].questionAnswerList[radio]
                        .standardAnswer
                }}
            </pre>
            </el-col>
            <el-col :span="12">
              <div style="height: 90px">
                <h3>您的答案</h3>
                <template>
                  请判断您的答案是否正确
                  <el-radio v-model="situation[$route.query.J]"
                            :label="1">正确</el-radio>
                  <el-radio v-model="situation[$route.query.J]"
                            :label="0">错误</el-radio>
                </template>
              </div>
              <div>
                <pre>{{ userAnswer[$route.query.J] }}
                    </pre>
              </div>
            </el-col>
          </el-row>
        </div>
        <div style="text-align: center">
          <el-button-group>
            <el-button type="primary"
                       v-show="Number($route.query.J) !== 0"
                       style="margin-top: 10px"
                       @click="$router.push('/judge?J=' + (Number($route.query.J) - 1))">上一题</el-button>

            <el-button type="primary"
                       v-show="Number($route.query.J) !== questions.length - 1"
                       style="margin-top: 10px"
                       @click="$router.push('/judge?J=' + (Number($route.query.J) + 1))">下一题</el-button>
            <el-button type="primary"
                       v-show="Number($route.query.J) === questions.length - 1"
                       style="margin-top: 10px"
                       @click="submitJudge">提交</el-button>
          </el-button-group>
        </div>
      </el-col>
    </el-row>
    <el-dialog :visible.sync="chooseDialog"
               width="50%"
               :show-close="false"
               :close-on-click-modal="false"
               :close-on-press-escape="false">
      <span>本次专项训练未通过，请选择是否继续训练该知识点：</span>
      <div style="margin-top: 20px">
        <el-button type="primary"
                   @click="chooseContinue">继续训练</el-button>
        <el-button type="primary"
                   @click="chooseTeacher">请教老师</el-button>
        <el-button type="primary"
                   @click="learnPoint">学习知识点</el-button>
        <!-- <el-button type="danger"
                   @click="chooseSkip">跳 过</el-button> -->
      </div>

    </el-dialog>

    <!-- 展示模式3的dialog -->
    <el-dialog title="提示"
               :visible="model3DialogVisible"
               width="30%">
      <span>这是一段信息</span>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="model3DialogVisible = false">取 消</el-button>
        <el-button type="primary"
                   @click="model3DialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import request from '@/api/request'
export default {
  name: 'Judge',
  data() {
    return {
      situation: [-1, -1, -1, -1, 1, -1, -1, -1, -1, -1],
      radio: 0,
      chooseDialog: false,
      chooseForm: {
        chooseResult: '',
      },
      model3DialogVisible: false,
    }
  },
  computed: {
    userAnswer() {
      let res = JSON.parse(window.sessionStorage.getItem('userAnswer'))
      return res
    },
    questions() {
      return JSON.parse(window.sessionStorage.getItem('questions'))
    },
    questionId() {
      let res = []
      for (let i = 0; i < this.questions.length; i++) {
        res.push(this.questions[i].questionId)
      }
      return res
    },
    testId() {
      return window.sessionStorage.getItem('testId')
    },
    testType() {
      return window.sessionStorage.getItem('testType')
    },
    statu() {
      let res = []
      for (let i = 0; i < this.situation.length; i++) {
        if (this.situation[i] == -1) {
          res.push(0)
        } else {
          res.push(this.situation[i])
        }
      }
      return res
    },
  },
  methods: {
    submitJudge() {
      this.$confirm(
        '确定要提交吗？确定后将不能更改，请检查是否判断完毕',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
        .then(() => {
          // 发送用户答案信息
          request({
            url: '/model2/check',
            method: 'POST',
            data: {
              userId: window.sessionStorage.getItem('token'),
              testId: this.testId, // 试卷Id
              testType: this.testType,
              questionId: this.questionId,
              userAnswer: this.userAnswer,
              situation: this.statu,
              // userLevel:sessionStorage.getItem("level"),
            },
          }).then((res) => {
            let data = res.data
            let status = data.status

            if (status == 0) {
              if (this.testType == 0) {
                this.chooseDialog = true
              } else {
                this.$alert('此次训练未通过，下次继续该知识点训练')
                this.$router.replace('/trainHome/training')
              }
            }

            if (status == 1) {
              this.$alert('恭喜你此次训练通过')
              this.$router.replace('/trainHome/training')
            }

            if (status == 3) {
              this.$alert(
                '恭喜你此次训练通过。\n本套题已做完，下次的套题难度不变'
              )
              this.$router.replace('/trainHome/training')
            }

            if (status == 4) {
              this.$alert('本次训练未通过。\n本套题已做完，下次套题的难度不变')
              this.$router.replace('/trainHome/training')
            }

            if (status == 5) {
              this.$alert(
                '恭喜你此次训练通过。\n本套题已做完，根据您的数据，您下次应该做提升一个难度的套题'
              )
              this.$router.replace('/trainHome/training')
            }

            if (status == 6) {
              this.$alert(
                '本次训练未通过。\n本套题已做完，根据您的数据，您下次应该做提升一个难度的套题'
              )
              this.$router.replace('/trainHome/training')
            }

            if (status == 7) {
              //直接跳到training，然后通过model3Active启动dialog
              console.log('status', status)
              this.$router.push({
                name: 'Training',
                params: {
                  status: status,
                },
              })
            }

            if (status == 8) {
              //直接跳到training，然后通过model3Active启动dialog
              console.log('status', status)
              this.$router.push({
                name: 'Training',
                params: {
                  status: status,
                },
              })
            }
          })
        })
        .catch(() => {
          console.log('取消提交')
        })
    },
    chooseQuestion(index) {
      this.$router.push({
        path: '/judge',
        query: { J: index },
      })
      this.radio = 0
    },
    chooseContinue() {
      console.log('chooseContinue')
      let allTestData = JSON.parse(window.sessionStorage.getItem('allTestData'))
      console.log(allTestData.specialTestLevel)
      console.log(allTestData.knowledgePoint)
      request({
        url: '/getAnotherSpecialTest',
        method: 'POST',
        data: {
          userId: sessionStorage.getItem('token'),
          specialTestLevel: allTestData.specialTestLevel,
          knowledgePoint: allTestData.knowledgePoint,
        },
      }).then((res) => {
        console.log(res)
      })
      this.chooseDialog = false
      this.$message({
        message: '已为您增加一套该知识点的训练',
        type: 'success',
      })
      this.$router.replace('/trainHome/training')
    },
    chooseTeacher() {
      console.log('chooseTeacher')
      this.$router.replace('/trainHome/message')
    },
    // chooseSkip() {
    //   console.log('chooseSkip')
    //   this.$router.replace('/trainHome/training')
    // },
    learnPoint() {
      console.log('learnPoint')
      this.$router.replace('/trainHome/learnKnowledge')
    },
  },
  mounted() {
    for (let i = 0; i < this.questionId.length; i++) {
      this.situation[i] = -1
    }
    let s = window.sessionStorage.getItem('complieSituation')
    if (s) {
      this.situation = JSON.parse(s)
    }
  },
  updated() {
    window.sessionStorage.setItem('situation', JSON.stringify(this.situation))
  },
  destroyed() {
    let sessionKeys = Object.keys(sessionStorage)
    for (let i = 0; i < sessionKeys.length; i++) {
      if (
        sessionKeys[i] != 'token' &&
        sessionKeys[i] != 'account' &&
        sessionKeys[i] != 'level'
      ) {
        window.sessionStorage.removeItem(sessionKeys[i])
      }
    }
  },
}
</script>

<style scoped>
* {
  white-space: pre-line;
}
.subject-item-title {
  font-size: 16px;
  margin-bottom: 10px;
}
.subject-item-title {
  font-size: 16px;
  margin-bottom: 10px;
}
.subject-item-title {
  padding: 10px 0;
  margin-bottom: 0;
  font-weight: 600;
}
.subject-describe {
  font-size: 14px;
  line-height: 1.8;
}
.subject-describe img {
  max-width: 100%;
  vertical-align: middle;
}
.subject-question {
  height: 22px;
  overflow: hidden;
}
.subject-question {
  height: auto;
}
.subject-question {
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 20px;
}
.subject-question ul {
  list-style: disc;
  padding-left: 30px;
  margin: 10px 0;
}
.subject-question ol {
  list-style: decimal;
  padding-left: 30px;
  margin: 10px 0;
}
.subject-question img {
  _width: 100%;
  max-width: 100%;
}
pre {
  font-size: 14px;
  position: relative;
  /* border-left: 2px solid #1abc9c; */
  padding: 10px 0 10px 10px;
  margin: 0.8em 0;
  background: #fafafa;
  word-break: break-word;
}
pre {
  font-family: Courier, 'Courier New', monospace;
  white-space: pre-wrap;
  word-wrap: break-word;
}
.question-oi {
  margin: 20px 0;
  font-size: 14px;
  color: #34495e;
}
.question-oi-hd {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 15px;
}
.question-oi-bd {
  border: 1px solid #e8edf2;
  background: #eaf0f5;
  padding: 10px;
  border-radius: 4px;
  position: relative;
}
.question-oi-tips {
  color: #999;
  font-size: 14px;
  font-weight: 400;
  margin-left: 10px;
}
.question-oi-mod {
  margin-bottom: 20px;
  position: relative;
}
.question-oi-mod:last-child {
  margin: 0;
}
.question-oi-mod h2 {
  margin-bottom: 10px;
  font-size: 14px;
}
.question-oi-mod p {
  line-height: 1.6;
}
.code-copy-btn {
  color: #8f9599;
  border: 1px solid #b4b8bb;
  border-radius: 3px;
  position: absolute;
  right: 0;
  top: 0;
  font-size: 12px;
  padding: 1px 4px;
}
.code-copy-btn:hover {
  background: #d2dae0;
  color: #8f9599;
}
/* header */
.header-bar {
  background: #232c31;
  color: #fff;
  line-height: 50px;
  font-size: 14px;
  overflow: hidden;
}
.header-bar .head-pic {
  float: right;
  margin: auto 20px;
  text-align: center;
}
.header-bar .head-pic > img {
  width: 35px;
  height: 35px;
  vertical-align: middle;
  border-radius: 50%;
}
.header-bar .header-left {
  float: left;
  padding: 0 30px 0 10px;
  height: 50px;
  margin-top: 10px;
}
.header-bar .header-right {
  float: right;
}
.header-bar .code-list-box {
  display: inline-block;
}
.header-bar .code-list-box li {
  display: inline-block;
  margin-right: 30px;
}
.header-bar .code-list-box li:last-child {
  margin-right: 0;
}
.header-bar .code-list-box li a {
  display: block;
  height: 24px;
  font-size: 14px;
  line-height: 24px;
  color: #fff;
}
.header-bar .code-list-box li a:hover {
  color: #25bb9b;
}
.header-bar .code-list-box i {
  font-size: 16px;
  margin-right: 3px;
}
.div-card {
  display: flex;
  align-items: center;
}
.card {
  margin: 20px 250px;
  width: 1000px;
  height: 150px;
}
.el-col {
  text-align: left;
}
.question-index {
  width: 50%;
}
.temp {
  position: absolute;
}
</style>
