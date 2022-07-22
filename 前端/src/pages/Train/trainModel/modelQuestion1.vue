<template>
  <div v-loading="!isReady" element-loading-background="#fff">
    <div class="header-bar">
      <div class="header-left">
        <div class="question-title">
          <i class="el-icon-s-flag"></i
          >{{ `${testType == 1 ? "普通训练" : "专项训练"}` }}
        </div>
      </div>
      <div class="header-right">
        <ul class="code-list-box">
          <li>
            <el-button type="text" @click="$confirm('退出则此次练习作废，确定退出?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=> {
            $router.push('/trainHome/training')

         }).catch(() => {

        });">
              <i class="el-icon-s-home"></i>
              训练主页
            </el-button>
          </li>
        </ul>
        <a href="" class="head-pic"> </a>
      </div>
    </div>
    <div class="div-card">
      <el-card class="card">
        <h3 style="text-align: center">{{ testName }}</h3>
        <div v-if="testType==1">
          <span>剩余时间：{{ hour }} : </span>
          <span>{{ minute }} : </span>
          <span>{{ second }}</span>
          <div class="block">
            <el-slider
                v-model="value"
                :format-tooltip="formatTooltip"
            ></el-slider>
          </div>
        </div>
        <p style="text-align: right; font-size: 10px; color: red">
          请勿离开此页面！
        </p>
      </el-card>
    </div>
    <el-row :gutter="20" style="margin: 0px"  v-if="isReady">
      <el-col :span="4" :offset="2">
        <h3>题目序号</h3>
        <el-row>
          <el-col
              :span="8"
              v-for="(question, index) in questions"
              :key="index"
              style="margin: 5px 0px"
          >
          <div class="question-index">
            <el-button
                :type="index===Number($route.query.Q)?'primary':''"
                circle
                @click="$router.push('/modelQuestion1?Q=' + index)"
                style="width:100%"
            >{{ index + 1 }}
            </el-button>
          </div>
            
          </el-col> </el-row
        ><br /><br /><br /><br />
        <el-button type="primary" @click="submitAnswer">提交</el-button>
      </el-col>
      <el-col :span="16">
        <div>
          <h2 class="subject-item-title">题目描述</h2>
          <div class="subject-describe">
            <div class="subject-question">
              <div>
                {{ questions[$route.query.Q].questionTitle }}
              </div>
              <div v-if="questions[$route.query.Q].questionImgList.length != 0">
                <div>
                  <img
                      alt=""
                      :src="`${
                      questions[$route.query.Q].questionImgList[0]
                        .questionImgUrl
                    }`"
                      style="height: auto; width: 120px"
                  /><br />
                </div>
              </div>
            </div>
            <el-collapse>
              <el-collapse-item
                  v-for="(questionIO, index) in questions[$route.query.Q]
                  .questionIOList"
                  :key="index"
                  :title="'示例' + (index + 1)"
              >
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
            <!-- <el-input
                type="textarea"
                :autosize="{ minRows: 10, maxRows: 20 }"
                placeholder="请输入内容"
                v-model="userAnswer[$route.query.Q]"
            >
            </el-input> -->
            <el-select v-model="language" @change="changeLanguage" placeholder="请选择" size="small" style="margin-top: 15px; width: 100px;">
              <el-option
                v-for="item in languageOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
            <codemirror
              v-model="userAnswer[$route.query.Q]"
              :options="cmOption"
              class="code-mirror"
              ref="myCmGenerate"
            ></codemirror>
          </div>
          <div style="text-align: center">
            <el-button-group >
              <el-button type="primary"
                         v-show="Number($route.query.Q)!==0"
                         style="margin-top: 10px"
                         @click="$router.push('/modelQuestion1?Q=' + (Number($route.query.Q)-1))"
              >上一题</el-button>

              <el-button type="primary"
                         v-show="Number($route.query.Q)!==questions.length-1"
                         style="margin-top: 10px;"
                         @click="$router.push('/modelQuestion1?Q=' + (Number($route.query.Q)+1))"
              >下一题</el-button>
              <el-button type="primary"
                         v-show="Number($route.query.Q)===questions.length-1"
                         style="margin-top: 10px;"
                         @click="submitAnswer"
              >提交</el-button>
            </el-button-group>
          </div>

        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import request from "@/api/request";
import { codemirror } from "vue-codemirror";
// 我这里引入的是JS语言文件
import "codemirror/mode/javascript/javascript.js";
import "codemirror/mode/python/python.js";
import "codemirror/mode/javascript/javascript.js";
import "../../../api/setting";
import "codemirror/theme/idea.css";

let timer = null;
export default {
  name: "modelQuestion1",
  components: { codemirror },
  data() {
    return {
      questions: [], // 题目
      userAnswer: ["", "", "", "", "", "", "", "", "", ""], // 用户答案
      testName: "", //试卷名称
      testType: 0, // 试卷类型
      value: 0, // el-slider标签的值
      hour: 0, // 倒计时-时
      minute: 0, // 倒计时-分
      second: 0, // 倒计时-秒
      endTime: 0, // 截止时间
      beginTime: 0, // 开始时间
      limitTime: 0, // 试卷时间
      isReady: false,// 控制页面显示，数据加载完后显示
      timer1: null,// 控制倒计时定时器开始的定时器

      language: "C", // 用户选择的答题语言
      languageOptions: [{
        value: 'C',
        lable: 'C'
      }, {
        value: 'Java',
        lable: 'Java'
      }, {
        value: 'Python',
        lable: 'Python'
      }],

      // codemirror 配置
      cmOption: {
        tabSize: 4, // tab
        styleActiveLine: true, // 高亮选中行
        lineNumbers: true, // 显示行号
        styleSelectedText: true,
        line: true,
        foldGutter: true, // 块槽
        autoCloseBrackets: true, //自动补全括号
        gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"],
        highlightSelectionMatches: { showToken: /\w/, annotateScrollbar: true }, // 可以启用该选项来突出显示当前选中的内容的所有实例
        mode: "text/x-csrc",
        // hint.js options
        hintOptions: {
          // 当匹配只有一项的时候是否自动补全
          completeSingle: false,
        },
        // 快捷键 可提供三种模式 sublime、emacs、vim
        keyMap: "sublime",
        matchBrackets: true,
        showCursorWhenSelecting: true,
        theme: "idea", // 主题
        extraKeys: { Ctrl: "autocomplete" }, // 可以用于为编辑器指定额外的键绑定，以及keyMap定义的键绑定
      },
    };
  },
  computed: {
    // 题目Id
    questionId() {
      let res = [];
      for (let i = 0; i < this.questions.length; i++) {
        res.push(this.questions[i].questionId);
      }
      return res;
    },
  },
  methods: {
    // 改变答题语言时触发
    changeLanguage(option) {
      // console.log(option);
      if (option === 'C') {
        this.cmOption.mode = 'text/x-csrc';
      } else if (option === 'Java') {
        this.cmOption.mode = 'text/x-java';
      } else {
        this.cmOption.mode = 'text/x-python';
      }
    },
    // 获得试题
    getTrain() {
      request({
        url: "/model/trainingstart",
        method: "post",
        data: {
          userId: sessionStorage.getItem("token"),


        },
      }).then((res) => {
        if (res.code == 0) {
          alert("题库中已无试卷，点击确定前往选择您需要训练的知识点");
          this.$router.push("/trainHome/setKnowledgePoints");
        } else {
          console.log(res);
          // 判断试题类型
          if (res.data.specialId) {
            this.testType = 0;
            this.testName = res.data.testName;
            this.questions = res.data.specialQuestions;
            // 将试卷类型写入session
            window.sessionStorage.setItem("testType", 0);
            // 将题目写入session
            window.sessionStorage.setItem(
                "questions",
                JSON.stringify(this.questions)
            );
            // 将试卷Id写入session
            window.sessionStorage.setItem("testId", res.data.specialId);
            this.isReady = true;
          } else {
            this.testType = 1;
            this.testName = res.data.testName;
            this.limitTime = res.data.limitTime;
            window.sessionStorage.setItem("limitTime", res.data.limitTime);
            this.questions = res.data.normalQuestions;
            // 将试卷类型写入session
            window.sessionStorage.setItem("testType", 1);
            // 将题目写入session
            window.sessionStorage.setItem(
                "questions",
                JSON.stringify(this.questions)
            );
            // 将试卷Id写入session
            window.sessionStorage.setItem("testId", res.data.normalId);
            this.isReady = true;
          }
        }
      });
    },
    submitAnswer() {
      this.$confirm('确定要提交吗？确定后将进入判题页面且不能返回', '提示', {
        confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
      }).then(()=> {
        clearInterval(timer);
      // 跳转到判题页面
      this.$router.replace("/judge1?J=0");
      }).catch(()=>{
        console.log('取消提交');
      })
    },
    // 倒计时函数
    countDown() {
      let nowTime = +new Date();
      let times = (this.endTime - nowTime) / 1000;
      if (times <= 0) {
        // this.submitAnswer();
      }
      let h = parseInt((times / 60 / 60) % 24); // 时
      let m = parseInt((times / 60) % 60); // 分
      let s = parseInt(times % 60); // 秒
      this.value =
          100 - (h * 3600 + m * 60 + s) * (100 / (this.limitTime * 60));
      this.hour = h < 10 ? "0" + h : h;
      this.minute = m < 10 ? "0" + m : m;
      this.second = s < 10 ? "0" + s : s;
      // console.log(this.hour);
      // console.log(this.minute);
      // console.log(this.second);
    },
    formatTooltip() {
      return this.hour + " : " + this.minute + " : " + this.second;
    },
    // 控制倒计时开启
    beginTimer() {
      if (this.endTime != 0) {
        clearInterval(this.timer1);
        return;
      }
      // 当数据初始化完成后，开启倒计时
      if (this.isReady) {
        if (this.testType == 1) {
        this.endTime = this.beginTime + this.limitTime * 60000;
        console.log("endTime" + this.endTime);
        timer = setInterval(this.countDown, 1000);
      }
      }
    }
  },
  mounted() {
    console.log("mounted");
    // 将用户答案从session中读取出来
    let userAnswer = window.sessionStorage.getItem("userAnswer");
    if (userAnswer) {
      this.userAnswer = JSON.parse(userAnswer);
    }
    this.time1 = setInterval(this.beginTimer, 100);
  },
  updated() {
    window.sessionStorage.setItem(
        "userAnswer",
        JSON.stringify(this.userAnswer)
    );
  },
  created() {
    this.getTrain();
    let beginTime = window.sessionStorage.getItem("beginTime");
    if (beginTime) {
      this.beginTime = parseInt(beginTime);
    } else {
      this.beginTime = +new Date();
      window.sessionStorage.setItem("beginTime", this.beginTime);
    }
    // console.log(this.beginTime);
  },
  destroyed() {
    clearInterval(timer);
  },
};
</script>

<style scoped>
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
  width: 1000px;
}
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
  border-left: 2px solid #1abc9c;
  padding: 10px 0 10px 10px;
  margin: 0.8em 0;
  background: #fafafa;
  word-break: break-word;
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
  height: auto;
}
.el-col {
  text-align: left;
}
.question-index {
  width: 50%;
}
</style>
