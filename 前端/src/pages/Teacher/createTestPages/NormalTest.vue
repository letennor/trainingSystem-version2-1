<template>
  <div>
    <!-- 需要传进去的参数 1. 试卷名称   2. 套题id   3. 试卷编号 4. 限定时间-->
    <el-row>
      <el-col :span="24">

        <el-input placeholder="请输入内容"
                  v-model="normalTestName">
          <template slot="prepend">试卷名称</template>
        </el-input>
      </el-col>

      <el-col :span="24">

        <el-input v-model="setId"
                  disabled>
          <template slot="prepend">套题Id</template>
        </el-input>
      </el-col>

      <el-col :span="24">

        <el-input v-model="normalTestNumber"
                  disabled>
          <template slot="prepend">试卷序号</template>
        </el-input>
      </el-col>

      <el-col :span="24">
        <span>限定时间：</span>
        <template>
          <el-input-number v-model="limitTime"
                           :min="0"
                           :max="180"
                           placeholder="限定时间"></el-input-number>
        </template>
      </el-col>

      <el-col :span="24">
        <el-button type="primary"
                   @click="createNormalTest">
          创建试卷
        </el-button>
      </el-col>

    </el-row>

  </div>
</template>


<script>
export default {
  name: 'NormalTest',
  data() {
    return {
      normalTestName: null,
      setId: null,
      limitTime: null,
      normalTestNumber: null,
    }
  },

  methods: {
    createNormalTest() {
      console.log('创建试卷')
      this.$router.push({
        name: 'SelectQuestion',
        params: {
          testName: this.normalTestName,
          questionNumber: 10,
          testType: 1,
          limitTime: this.limitTime,
          normalTestNumber: this.$route.params.normalTestNumber,
          setId: this.setId,
        },
      })
    },
  },

  mounted() {
    this.normalTestNumber = this.$route.params.normalTestNumber + 1
    this.setId = this.$route.params.setId
  },
}
</script>

<style scoped>
.el-row {
  width: 400px;

  height: 500px;

  position: relative;

  left: 0;

  right: 0;

  top: 0;

  bottom: 0;

  margin: auto;

  margin-top: 20px;
}

.el-col {
  margin-top: 20px;
}

.el-button {
  font-size: 15px;
  width: 400px;
}
/* 
* {
  border: 1px red solid;
} */
</style>