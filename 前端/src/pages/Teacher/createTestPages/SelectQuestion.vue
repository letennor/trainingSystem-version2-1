<template>
  <div>
    <el-row>
      <el-button @click="drawer1 = true"
                 type="primary"
                 style="margin-left: 16px;">
        选择知识点
      </el-button>

      <el-drawer title="选择知识点"
                 :visible.sync="drawer1"
                 :with-header="false"
                 size="31%">
        <div>
          <el-checkbox-group v-model="selectKnowledgePointList">
            <el-row :gutter="10">
              <el-checkbox-button v-for="knowledgePoint in knowledgePointList"
                                  :label="knowledgePoint"
                                  :key="knowledgePoint">{{knowledgePoint}}</el-checkbox-button>
            </el-row>

          </el-checkbox-group>
        </div>
      </el-drawer>

      <el-button @click="drawer2 = true"
                 type="primary"
                 style="margin-left: 16px;">
        选择题目类型
      </el-button>

      <el-drawer title="选择题目类型"
                 :visible.sync="drawer2"
                 :with-header="false"
                 size="31%">
        <div>
          <el-checkbox-group v-model="selectTypeList">
            <el-row :gutter="10">
              <el-checkbox-button v-for="type in typeList"
                                  :label="type.id"
                                  :key="type.id">{{type.name}}</el-checkbox-button>
            </el-row>

          </el-checkbox-group>
        </div>
      </el-drawer>

      <el-button @click="drawer3 = true"
                 type="primary"
                 style="margin-left: 16px;">
        选择题目难度
      </el-button>

      <el-drawer title="选择题目难度"
                 :visible.sync="drawer3"
                 :with-header="false"
                 size="31%">
        <div>
          <el-checkbox-group v-model="selectLevelList">
            <el-row :gutter="10">
              <el-checkbox-button v-for="level in LevelList"
                                  :label="level.id"
                                  :key="level.id">{{level.name}}</el-checkbox-button>
            </el-row>

          </el-checkbox-group>
        </div>
      </el-drawer>

      <!-- 点击查找 -->
      <el-button icon="el-icon-search"
                 type='primary'
                 style="margin-left:30px"
                 @click="searchQuestion"></el-button>

      <!-- 点击提交  -->
      <el-button type="primary"
                 style="float:right"
                 @click="submitChoosedQuestion">提交<i class="el-icon-upload el-icon--right"></i></el-button>

    </el-row>

    <hr>

    <el-row :gutter="10"
            style="margin-left: 16px;">
      <el-col :span="24"
              v-for="question in questionList"
              :key="question.questionId"
              style="margin-bottom:10px"
              @click.native="clickQuestion(question)">
        <el-card shadow="hover">
          <el-row :gutter="10">

            <el-col :span="22">
              {{question.questionTitle}}

            </el-col>

            <template v-if="question.isChoosed">
              <div>
                <el-col :span="2">
                  <el-tag type="success"
                          size="medium">已选择</el-tag>
                </el-col>

              </div>
            </template>

            <template v-if="!question.isChoosed">
              <div>
                <el-col :span="2">
                  <el-tag type="info"
                          size="medium">未选择</el-tag>
                </el-col>

              </div>
            </template>

          </el-row>

        </el-card>
      </el-col>
    </el-row>
    <el-backtop :bottom="60"><i class="el-icon-caret-top"></i></el-backtop>
  </div>
</template>
<script>
import request from '@/api/request'

export default {
  name: 'SelectQuestion',
  data() {
    return {
      drawer1: false,
      drawer2: false,
      drawer3: false,
      //所有知识点类型
      knowledgePointList: [
        '数组',
        '字符串',
        '排序',
        '字符串匹配',
        '桶排序',
        '计数排序',
        '动态规划',
        '深度优先搜索',
        '贪心算法',
        '广度优先搜索',
        '二分查找',
        '回溯',
        '递归',
        '分治',
        '哈希表',
        '树',
        '二叉树',
        '堆',
        '图',
        '链表',
        '二叉搜索树',
        '队列',
        '双向链表',
        '双指针',
        '单调栈',
        '归并排序',
        '有序集合',
        '拓扑排序',
        '枚举',
      ],
      //选择了的知识点类型
      selectKnowledgePointList: [],
      //搜索的题目类型
      typeList: [
        {
          name: '数据结构',
          id: 2,
        },
        {
          name: '算法',
          id: 3,
        },
      ],
      //选择了的题目类型
      selectTypeList: [],
      //所有题目难度
      LevelList: [
        {
          name: '简单',
          id: 1,
        },
        {
          name: '普通',
          id: 2,
        },
        {
          name: '困难',
          id: 3,
        },
      ],
      //选择搜索的题目难度
      selectLevelList: [],
      //搜索出来的所有题目
      questionList: [],
      //选择了的题目id
      selectedQuestionIdList: [],
      //已选择的题目的个数
      currentChoosedNumber: 0,
    }
  },
  computed: {
    //判断当前所选题目是否已满
    isQuestionNumberFull() {
      if (this.currentChoosedNumber === this.$route.params.questionNumber) {
        return true
      } else {
        return false
      }
    },
  },
  methods: {
    searchQuestion() {
      //开始发送请求查找题目
      request({
        url: '/getAllQuestion',
        method: 'post',
        data: {
          knowledgePointList: this.selectKnowledgePointList,
          questionLevelList: this.selectedQuestionIdList,
          questionTypeList: this.selectTypeList,
        },
      }).then((res) => {
        this.questionList = res.data
        //为每一个数据增加isChoosed属性
        this.questionList.forEach((myQuestion) => {
          this.$set(myQuestion, 'isChoosed', false)
        })
      })
    },

    clickQuestion(question) {
      //将传过来的quesitonId放入容器中
      //循环数组，改变isChoosed
      this.questionList.forEach((myQuestion) => {
        if (myQuestion.questionId === question.questionId) {
          if (myQuestion.isChoosed) {
            //表名这条题目是已经勾选了的，需要取消勾选
            this.currentChoosedNumber = this.currentChoosedNumber - 1
            myQuestion.isChoosed = !myQuestion.isChoosed
            this.$message({
              type: 'info',
              message: `已选择题目:${this.currentChoosedNumber}/${this.$route.params.questionNumber}`,
            })
          } else {
            //表名这条题目是刚刚要勾选的
            //判断是否已满
            if (this.isQuestionNumberFull) {
              this.$message({
                type: 'error',
                message: '选择题目已满！',
              })
            } else {
              myQuestion.isChoosed = !myQuestion.isChoosed
              this.currentChoosedNumber = this.currentChoosedNumber + 1
              this.$message({
                type: 'info',
                message: `已选择题目:${this.currentChoosedNumber}/${this.$route.params.questionNumber}`,
              })
            }
          }
        }
      })
    },

    submitChoosedQuestion() {
      //将所有被选中的题目放入容器中
      this.questionList.forEach((myQuestion) => {
        if (myQuestion.isChoosed) {
          console.log('选中了', myQuestion.questionId)
          this.selectedQuestionIdList.push(myQuestion.questionId)
        }
      })

      //判断：如果testType = 0,则为专项，否则为普通
      if (this.$route.params.testType === 0) {
        //发送请求，将试卷保存在数据库中
        request({
          url: '/specialQuestion/receive',
          method: 'post',
          data: {
            questionIdList: this.selectedQuestionIdList,
            testName: this.$route.params.testName,
            knowledgePointList: this.selectKnowledgePointList,
            specialLevel: this.$route.params.specialLevel,
            threshold: this.$route.params.specialThreshold,
            userId: sessionStorage.getItem('token'),
          },
        }).then((res) => {
          console.log(res)
        })
      }

      //为普通练习
      if (this.$route.params.testType === 1) {
        console.log('这是普通练习试卷提交')
        console.log('normalTestNumber', this.$route.params.normalTestNumber)
        request({
          url: '/createNormalTest',
          method: 'post',
          data: {
            normalTestNumber: this.$route.params.normalTestNumber + 1,
            testName: this.$route.params.testName,
            limitTime: this.$route.params.limitTime,
            setId: this.$route.params.setId,
            questionIdList: this.selectedQuestionIdList,
          },
        })
      }

      //上传选择了的题目
      //将选择了的题目放入数组中

      this.$message({
        type: 'success',
        message: '操作成功!',
      })

      setTimeout(() => {
        //返回制作试题页面
        this.$router.push({
          name: 'CreateTest',
        })
      }, 100)
    },
  },
}
</script>


<style scoped>
.el-checkbox-button {
  margin-bottom: 20px;
  margin-left: 30px;
  margin-top: 10px;
  font-size: 40px;
}

* {
  white-space: pre-line;
}
</style>