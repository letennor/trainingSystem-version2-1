<template>
  <div>

    <el-row>

      <el-col>
        <el-button type="primary"
                   @click="back"
                   style="width: 200px; float: right; margin-bottom: 10px">返回</el-button>

      </el-col>
      <el-col :span="24"
              style="margin-top: 0">
        <el-card shadow="always">

          <el-descriptions class="margin-top"
                           title="题目">

          </el-descriptions>

          {{questionBasicInfo.questionTitle}}

        </el-card>
      </el-col>

      <el-col :span="24">
        <el-card shadow="always">
          <el-descriptions title="题目基本信息"
                           :column="2"
                           style="font-size: 20px">
            <el-descriptions-item label="题目类型">{{questionBasicInfo.questionType}}</el-descriptions-item>
            <el-descriptions-item label="难度">{{level}}</el-descriptions-item>
            <el-descriptions-item label="知识点">{{questionBasicInfo.knowledgePoint}}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{renderTime(questionBasicInfo.gmtCreate)}}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>

      <el-col :span="24">

        <el-card>

          <el-collapse>

            <el-collapse-item title="IO示例"
                              style="font-size:20px">

              <el-card shadow="hover"
                       v-for="io in questionIOList"
                       :key="io.questionIoId">
                <div class="question-oi-bd">
                  <div class="question-oi-mod">
                    <h2>输入:</h2>
                    <div class="question-oi-cont">
                      <pre>{{ io.input }}</pre>
                    </div>
                  </div>
                  <div class="question-oi-mod">
                    <h2>输出:</h2>
                    <pre>{{ io.output }}</pre>
                  </div>

                </div>
              </el-card>

            </el-collapse-item>

          </el-collapse>
        </el-card>

      </el-col>

      <el-col :span="24">
        <el-card>
          <el-descriptions title="答案"
                           :column="2"
                           style="font-size: 20px">

          </el-descriptions>

          <el-collapse>

            <el-collapse-item title="C语言答案"
                              style="margin-top:15px;">
              <div>
                <pre>
                  {{cAnswer}}
                </pre>

              </div>
            </el-collapse-item>

            <el-collapse-item title="Java语言答案"
                              style="margin-top:15px">
              <pre>
                  {{javaAnswer}}
              </pre>
            </el-collapse-item>

            <el-collapse-item title="python语言答案"
                              style="margin-top:15px">
              <pre>
                  {{pythonAnswer}}
              </pre>
            </el-collapse-item>

          </el-collapse>
        </el-card>
      </el-col>

      <el-col :span="24">
        <el-card>
          <el-descriptions title="评论"
                           style="font-size: 20px">
          </el-descriptions>

          <comment :question="questionBasicInfo.questionId" />

        </el-card>
      </el-col>

    </el-row>
  </div>
</template>
<script>
import request from '../../../api/request'
import comment from '../../Train/comment/comment.vue'

export default {
  name: 'QuestionDetail',

  components: {
    comment,
  },

  data() {
    return {
      question: null,
      questionAnswerList: [],
      questionBasicInfo: [],
      questionIOList: [],
      questionImgList: [],
      questionType: null,
      level: null,
      knowledgePoint: null,
      javaAnswer: null,
      cAnswer: null,
      pythonAnswer: null,
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

    //取得这条题目所有信息
    getAllDetail() {
      request({
        url: '/questionDetails',
        method: 'post',
        data: {
          questionId: this.question.questionId,
        },
      }).then((res) => {
        let data = res.data

        console.log(data)
        this.questionAnswerList = data.questionAnswerList
        this.questionBasicInfo = data.questionBasicInfo
        this.questionIOList = data.questionIOList
        this.questionImgList = data.questionImgList

        //判断问题基本信息
        if (this.questionBasicInfo.questionType === 1) {
          this.questionType = '语言基础'
        } else if (this.questionBasicInfo.questionType === 2) {
          this.questionType = '数据结构'
        } else {
          this.questionType = '算法'
        }

        if (this.questionBasicInfo.questionLevel === 1) {
          this.level = '简单'
        } else if (this.questionBasicInfo.length === 2) {
          this.level = '普通'
        } else {
          this.level = '困难'
        }

        this.questionAnswerList.forEach((answer) => {
          if (answer.language === 1) {
            this.cAnswer = answer.standardAnswer
          }

          if (answer.language === 2) {
            this.javaAnswer = answer.standardAnswer
          }

          if (answer.language === 3) {
            this.pythonAnswer = answer.standardAnswer
          }
        })
      })
    },

    back() {
      //返回上一级
      if (this.$route.params.from === 'SpecialTestManagement') {
        this.$router.push({
          name: 'QuestionManagement',
          params: {
            specialId: this.$route.params.specialId,
            from: this.$route.params.from,
          },
        })
      }

      if (this.$route.params.from === 'SourceManagement') {
        this.$router.push({
          name: 'QuestionManagement',
          params: {
            specialId: this.$route.params.specialId,
            from: this.$route.params.from,
          },
        })
      }

      if (this.$route.params.from === 'NormalTestManagement') {
        this.$router.push({
          name: 'QuestionManagement',
          params: {
            normalId: this.$route.params.normalId,
            from: this.$route.params.from,
            setId: this.$route.params.setId,
          },
        })
      }
    },
  },

  mounted() {
    this.question = this.$route.params.question
    console.log('from', this.$route.params.from)
    this.getAllDetail()
  },
}
</script>

<style scoped>
* {
  white-space: pre-line;
}

.el-col {
  margin-top: 20px;
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
pre {
  font-family: Courier, 'Courier New', monospace;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-size: 15px;
}
</style>
