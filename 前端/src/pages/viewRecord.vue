<template>
  <div  v-if="isReady">
    <div class="header-bar">
      <div class="header-left">
        <div class="question-title">
          <i class="el-icon-s-flag"></i
          >{{
            `${testType == 1 ? "普通训练 " : "专项训练 —— "}` + knowledgePoint
          }}
        </div>
      </div>
      <div class="header-right">
        <ul class="code-list-box">
          <li>
            <router-link to="/trainHome/practiceRecord" class="code-list-is">
              <i class="el-icon-s-home"></i>
              返回
            </router-link>
          </li>
        </ul>
        <a href="" class="head-pic"> </a>
      </div>
    </div>
    <el-row :gutter="20" style="margin-top: 50px">
      <el-col :span="3" :offset="1">
        <h3>题目序号</h3>
        <el-row>
          <el-col
            :span="8"
            v-for="(question, index) in recordInfo"
            :key="index"
            style="margin: 5px 0px"
          >
          <div class="question-index">
            <el-button
              :type="index===Number($route.query.Q)?'primary':''"
              circle
              @click="chooseQuestion(index)"
              style="width:100%"
              ref="buttonIndex"
              >{{ index + 1 }}
            </el-button>
          </div>
            
          </el-col> </el-row
        ><br /><br /><br /><br />
        <!-- <el-button type="primary" @click="submitAnswer">提交</el-button> -->
      </el-col>
      <el-col :span="20">
        <div>
          <h2 class="subject-item-title">题目描述</h2>
          <div class="subject-describe">
            <div class="subject-question">
              <div>
                {{ recordInfo[$route.query.Q].questionTitle }}
              </div>
              <el-row :gutter="10">
                <el-col :span="12">
                  <div>
                    <h3>参考思路</h3>
                    <el-radio-group v-model="radio">
                      <el-radio-button
                        v-for="(answer, index) in recordInfo[$route.query.Q]
                          .questionAnswerList"
                        :key="index"
                        :label="index"
                        >{{
                          answer.language == 1
                            ? "C"
                            : answer.language == 2
                            ? "Java"
                            : "Python"
                        }}</el-radio-button
                      >
                    </el-radio-group>
                  </div>
                  <pre
                    >{{
                      radio == 0
                        ? recordInfo[$route.query.Q].questionAnswerList[0]
                            .standardAnswer
                        : recordInfo[$route.query.Q].questionAnswerList[radio]
                            .standardAnswer
                    }}
            </pre
                  >
                </el-col>
                <el-col :span="12">
                  <h3 style="display: inline-block; margin-right: 10px">
                    您的答案
                  </h3>
                  <el-tag
                    size="small"
                    type="success"
                    v-if="recordInfo[$route.query.Q].situation == 1"
                    >正确</el-tag
                  >
                  <el-tag size="small" type="danger" v-else>错误</el-tag>
                  <div style="background: #fafafa; margin-top: 40px">
                    <pre
                      >{{ recordInfo[$route.query.Q].userAnswer }}
                    </pre>
                  </div>

                </el-col>

              </el-row>
              <div style="text-align: center">
              <el-button-group >
                <el-button type="primary"
                           v-show="Number($route.query.Q)!==0"
                           style="margin-top: 10px"
                           @click="Choose1(Number($route.query.Q))"
                >上一题</el-button>

                <el-button type="primary"
                           v-show="Number($route.query.Q)!==recordInfo.length-1"
                           style="margin-top: 10px;"
                           @click="Choose2(Number($route.query.Q))"
                >下一题</el-button>
              </el-button-group>
            </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    <comment v-bind:question="recordInfo[$route.query.Q].questionId"></comment>
  </div>
</template>

<script>
import request from "@/api/request";
import comment from "@/pages/Train/comment/comment";
export default {
  name: "ViewRecord",
  components:{
    comment
  },
  data() {
    return {
      recordInfo: [],
      testType: -1,
      knowledgePoint: "",
      testId: "",
      radio: 0,
      isReady: false,
    };
  },
  methods: {
    // 获得该次记录的信息
    getRecordInfo() {
      request({
        url: "/student/testresult",
        method: "post",
        data: {
          userId: window.sessionStorage.getItem("token"),
          testId: this.$route.query.testId,
        }, //JSON.stringify({userId: window.sessionStorage.getItem("token"), testId: this.$route.query.testId}),
      }).then((res) => {
        console.log(res);
        this.recordInfo = res.data;
        this.knowledgePoint = res.data[0].knowledgePoint;
        this.testId = res.data[0].testId;
        if (res.data[0].testType == 0) {
          this.testType = 0;
        } else {
          this.testType = 1;
        }
        this.isReady = true;
      });
    },
    chooseQuestion(index) {
      this.$router.replace({
        path: "/viewRecord",
        query: { testId: this.testId, Q: index },
      });
      this.radio = 0;
    },
    Choose1(index){
      this.$router.push(({
        path: "/viewRecord",
        query: { testId: this.testId, Q: index-1 },
      }));
      this.radio = 0;
    },
    Choose2(index){
      this.$router.push(({
        path: "/viewRecord",
        query: { testId: this.testId, Q: index+1 },
      }));
      this.radio = 0;
    },
  },

  beforeUpdate() {
    // this.radio = 0;
    // console.log("beforeUpdate");
    // console.log(this.radio);
  },
  updated() {
    // console.log("Update");
    // console.log(this.radio);
  },
  mounted() {
    // console.log("mounted");
    this.getRecordInfo();
  },
};
</script>

<style scoped>
a {
  text-decoration: none;
}
/* 显示换行符 */
* {
  white-space: pre-line;
}
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}

.box-card {
  width: 480px;
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
/* 题目描述 */
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
  margin: 0 0;
  background: #fafafa;
  word-break: break-word;
  overflow: auto;
}
pre {
  font-family: Courier, "Courier New", monospace;
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
.question-index {
  width: 70%;
}
</style>
