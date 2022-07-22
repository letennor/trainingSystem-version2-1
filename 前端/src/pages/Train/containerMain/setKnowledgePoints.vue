<template>
  <div>
    <h3 style="margin-left: 120px">请选择你想训练的知识点：</h3>
    <el-row :gutter="20">
      <el-col :span="20" :offset="2">
        <el-form :model="setPointForm" ref="pointForm" :rules="rules"  style="padding-top: 20px; padding-left: 20px">
          <h5 style="margin: 0">数据结构：</h5>
          <el-form-item prop="knowledgePoints1">
            <el-checkbox-group v-model="setPointForm.knowledgePoints1">
              <el-row :gutter="20">
                <el-col :span="3" v-for="(point, index) in points1" :key="index"
                  ><el-checkbox-button :label="point">{{
                    point
                  }}</el-checkbox-button>
                </el-col>
              </el-row>
            </el-checkbox-group>
          </el-form-item>
          <h5 style="margin-bottom: 0">算法：</h5>
          <el-form-item prop="knowledgePoints2">
            <el-checkbox-group v-model="setPointForm.knowledgePoints2">
              <el-row :gutter="20">
                <el-col :span="3" v-for="(point, index) in points2" :key="index"
                  ><el-checkbox-button :label="point">{{
                    point
                  }}</el-checkbox-button>
                </el-col>
              </el-row>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item
            prop="specialLevel"
            label="请选择难度："
            style="text-align: left"
          >
            <el-radio-group v-model="setPointForm.specialLevel">
              <el-radio-button label="2" style="width: 100px"
                >难度2</el-radio-button
              >
              <el-radio-button label="3" style="width: 100px"
                >难度3</el-radio-button
              >
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="sendPoint">提 交</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <h3 style="margin-left: 120px">您也可以选择系统生成试题:</h3>
    <el-button
      type="primary"
      style="margin-left: 140px;margin-top:20px"
      @click="chooseSysCreate"
      >选择系统生成</el-button
    >
  </div>
</template>

<script>
import request from "@/api/request";
export default {
  name: "SetKnowledgePoints",
  data() {
    return {
      rules: {
        knowledgePoints1: [
          {
            type: "array",
            required: true,
            message: "请至少选择一个难度",
            trigger: "change",
          },
        ],
        knowledgePoints2: [
          {
            type: "array",
            required: true,
            message: "请至少选择一个难度",
            trigger: "change",
          },
        ],
        specialLevel: [{ required: true, message: "请选择一个难度" }],
      },
      knowledgePoints: [],
      specialLevel: 0,
      points1: [
        "数组",
        "字符串",
        "字符串匹配",
        "链表",
        "双向链表",
        "双指针",
        "队列",
        "单调栈",
        "树",
        "二叉树",
        "二叉搜索树",
        "堆",
        "图",
        "哈希表",
        "有序集合",
      ],
      points2: [
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
      ],
      setPointForm: {
        knowledgePoints1: [],
        knowledgePoints2: [],
        specialLevel: "",
      },
    };
  },
  methods: {
    sendPoint() {
      this.$refs.pointForm.validate((valid) => {
        if (valid) {
          console.log(this.setPointForm.knowledgePoints1);
          console.log(this.setPointForm.knowledgePoints2);
          console.log(this.setPointForm.knowledgePoints1.concat(this.setPointForm.knowledgePoints2));
          this.$confirm(
            "是否确认提交？请确认已选择好需要练习的知识点。",
            "提示",
            {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            }
          )
            .then(() => {
              request({
                url: "/model3/choose",
                method: "post",
                data: {
                  userId: window.sessionStorage.getItem("token"),
                  knowledgePoint: this.setPointForm.knowledgePoints1.concat(this.setPointForm.knowledgePoints2),
                  specialLevel: this.setPointForm.specialLevel,
                },
              }).then((res) => {
                if (res.code == 200) {
                  this.$message({
                    message: "提交成功，您已可以进入训练",
                    type: "success",
                  });
                  this.$router.replace("/trainHome/training");
                }
              });
            })
            .catch(() => {
              console.log("取消提交");
            });
        }
      });
    },
    chooseSysCreate() {
      this.$confirm(
        "此操作会使系统通过您的历史训练数据自动为您生成试题，是否继续？",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          request({
            url: "/model3/systemGenerate",
            method: "POST",
            data: {
              userId: sessionStorage.getItem("token"),
            },
          }).then((res) => {
            console.log(res + "系统生成");
            this.$router.replace("/trainHome/training");
          });
        })
        .catch(() => {
          this.$message({
            message: "已取消",
            type: "info",
          });
        });
    },
  },
};
</script>

<style scoped>
/* .el-col {
  text-align: center;
} */
.el-checkbox-button {
  margin: 10px;
}
.el-col label {
  width: 100%;
}
.el-col /deep/ label > span {
  width: 100%;
}
</style>
