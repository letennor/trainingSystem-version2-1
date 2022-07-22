<template>
  <div>
    <div class="header-bar">
      <div class="header-left">
        <div class="question-title"><i class="el-icon-s-flag"></i>水平测试</div>
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

    <h1 style="text-align: center">水平测试</h1>
    <p style="text-align: right; font-size: 20px; color: red">
      请勿离开此页面！
    </p>
    <el-divider></el-divider>
    <div v-for="(i, index) in questions"
         :key="'test1' + index"
         style="margin-left: 50px; margin-right: 50px">
      <el-card style="margin-top: 10px">
        <div class="text item">
          {{ index + 1 }}. {{ questions[index].questionTitle }}
          <el-input type="textarea"
                    style="margin-top: 5px"
                    :autosize="{ minRows: 2, maxRows: 4 }"
                    placeholder="请输入内容"
                    v-model="userAnswer[index]">
          </el-input>
        </div>
      </el-card>
    </div>
    <div style="margin-top: 20px; text-align: center">
      <el-button type="primary"
                 icon="el-icon-check"
                 @click="submitAnswer">提交</el-button>
    </div>
  </div>
</template>

<script>
import request from '@/api/request'
export default {
  name: 'LevelTest',
  data() {
    return {
      message: '', //当水平测试完成后返回的消息
      questions: [],
      userAnswer: [
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
        '',
      ],
    }
  },
  computed: {
    questionId() {
      let res = []
      for (let i = 0; i < this.questions.length; i++) {
        res.push(this.questions[i].questionId)
      }

      return res
    },
  },
  methods: {
    getLevelTest() {
      request({
        url: '/train/initLevel',
        method: 'get',
      }).then((res) => {
        console.log(res)
        this.questions = res.data
      })
    },

    // activeGuild() {
    //   this.$bus.$emit('guild', 123)
    // },

    submitAnswer() {
      this.$confirm('是否确认提交？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          request({
            url: '/train/initLevel',
            method: 'POST',
            data: {
              userId: window.sessionStorage.getItem('token'),
              questionId: this.questionId,
              userAnswer: this.userAnswer,
            },
          }).then((res) => {
            let level = res.data
            //判断不同的level设置不同的message

            if (level == 2) {
              this.message =
                '看来您数据结构方面的知识还有待欠缺哦，从数据结构开始吧~'
            }

            if (level == 3) {
              this.message = '看来您算法方面的知识还有待欠缺哦，从算法开始吧~'
            }

            if (level == 4) {
              this.message =
                '太棒了，您数据结构和算法的基础很扎实，让我们直接开始套题训练吧~'
            }

            this.$message({
              message: this.message,
              type: 'success',
            })

            window.sessionStorage.setItem('level', res.data)
            //发送一个信息，触发新手引导
            // this.activeGuild()
            this.$router.replace({ path: '/trainHome/training' })
          })
        })
        .catch(() => {
          console.log('取消提交')
        })
    },
  },
  created() {
    this.getLevelTest()
  },
  mounted() {
    let userAnswer = window.sessionStorage.getItem('userAnswer')
    if (userAnswer) {
      this.userAnswer = JSON.parse(userAnswer)
    }
  },
  updated() {
    window.sessionStorage.setItem('userAnswer', JSON.stringify(this.userAnswer))
  },
  destroyed() {
    window.sessionStorage.removeItem('userAnswer')
  },
}
</script>

<style>
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
</style>
