<template>
  <div>
    <el-row>
      <el-col :span="24">
        <h4>题目</h4>
        <el-input type="textarea"
                  :rows="8"
                  placeholder="题目"
                  v-model="questionTitle"
                  style="font-size: 20px">
        </el-input>

      </el-col>

      <el-col :span="12"
              style="margin-top:10px">
        <h4>题目类型</h4>
        <div>
          <el-radio v-model="questionType"
                    label="1"
                    border>语言基础</el-radio>
          <el-radio v-model="questionType"
                    label="2"
                    border>数据结构</el-radio>
          <el-radio v-model="questionType"
                    label="3"
                    border>算法</el-radio>
        </div>
      </el-col>

      <el-col :span="12"
              style="margin-top: 10px">
        <h4>题目难度</h4>
        <el-rate v-model="questionLevel"
                 show-text
                 :texts="texts"
                 :max="3">
        </el-rate>

      </el-col>

      <el-col style="margin-top: 10px">
        <h4>题目知识点</h4>
        <el-button @click="drawer = true"
                   type="primary"
                   style="width: 400px">
          选择知识点
        </el-button>

        <el-drawer title="选择知识点"
                   :visible.sync="drawer"
                   :with-header="false"
                   size="31%">
          <div>
            <el-radio-group v-model="selectKnowledgePoint">
              <el-row :gutter="10">
                <el-radio-button v-for="knowledgePoint in knowledgePointList"
                                 :label="knowledgePoint"
                                 :key="knowledgePoint">{{knowledgePoint}}</el-radio-button>
              </el-row>

            </el-radio-group>
          </div>
        </el-drawer>
      </el-col>

      <el-col :span="24">
        <h4>答案</h4>
        <el-input type="textarea"
                  :rows="8"
                  placeholder="答案（若有多个答案，以“；”分隔）"
                  v-model="standardAnswer"
                  style="font-size: 20px">
        </el-input>

      </el-col>

      <el-col :span="24">
        <el-button type="success"
                   style="float:left; margin-top: 50px; width: 200px; height:50px; font-size: 20px"
                   @click="submitModify">提交修改</el-button>

        <el-button type="primary"
                   style="float:right; margin-top: 50px; width: 200px; height:50px; font-size: 20px"
                   @click="back">返回</el-button>

      </el-col>

    </el-row>
  </div>
</template>
<script>
import request from '../../../api/request'

export default {
  name: 'ModifyQuestionType1',

  data() {
    return {
      questionId: null,
      questionTitle: null,
      questionType: null,
      texts: ['简单', '普通', '困难'],
      questionLevel: null,
      selectKnowledgePoint: null,
      drawer: false,
      standardAnswer: null,
    }
  },

  computed: {
    knowledgePointList() {
      if (this.questionType === '1') {
        return [
          'C基本语法',
          'C输入输出',
          'C数据类型',
          'C变量与常量',
          'C运算符',
          'C判断与循环',
          'C函数',
          'C数组',
          'C指针',
          'C字符串',
          'C结构体',
        ]
      }

      if (this.questionType === '2') {
        return [
          '数组',
          '字符串 ',
          '字符串匹配',
          '链表',
          '双向链表',
          '双指针',
          '队列',
          '单调栈',
          '树',
          '二叉树',
          '二叉搜索树',
          '堆',
          '图',
          '哈希表',
          '有序集合',
        ]
      }

      if (this.questionType === '3') {
        return [
          '排序',
          '桶排序',
          '计数排序',
          '归并排序',
          '拓扑排序',
          '二分查找',
          '回溯',
          '递归',
          '分治',
          '动态规划',
          '贪心算法',
          '深度优先搜索',
          '广度优先搜索',
        ]
      }

      return []
    },
  },

  methods: {
    back() {
      this.$router.push({
        name: 'QuestionManagement',
      })
    },

    submitModify() {
      //提交修改
      request({
        url: '/modifyQuestion',
        method: 'post',
        data: {
          questionId: this.questionId,
          questionTitle: this.questionTitle,
          standardAnswer: this.standardAnswer,
          questionType: this.questionType,
          questionLevel: this.questionLevel,
          kowledgePoint: this.selectKnowledgePoint,
          questionForm: 1,
        },
      }).then((res) => {
        console.log(res)
        this.$message({
          type: 'success',
          message: '修改成功！',
        })

        this.$router.push({
          name: 'QuestionManagement',
        })
      })
    },
  },

  mounted() {
    //将答案放入模板
    this.standardAnswer =
      this.$route.params.questionAnswerList[0].standardAnswer
    this.questionTitle = this.$route.params.questionBasicInfo.questionTitle
    this.questionType = this.$route.params.questionBasicInfo.questionType + ''
    this.questionLevel = this.$route.params.questionBasicInfo.questionLevel
    ;(this.questionId = this.$route.params.questionBasicInfo.questionId),
      (this.selectKnowledgePoint =
        this.$route.params.questionBasicInfo.knowledgePoint)
  },
}
</script>

<style scoped>
</style>