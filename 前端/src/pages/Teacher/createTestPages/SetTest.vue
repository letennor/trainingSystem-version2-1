<template>
  <div>
    <!-- 套题难度、 -->
    <el-row style="margin: auto; margin-top: 50px; width: 500px">
      <el-col :span="24">
        <div>
          <el-radio v-model="setLevel"
                    label=1
                    border>简单</el-radio>
          <el-radio v-model="setLevel"
                    label=2
                    border>普通</el-radio>

          <el-radio v-model="setLevel"
                    label=3
                    border>困难</el-radio>
        </div>
      </el-col>

      <el-col :span="24"
              style="margin-top: 20px">
        <span>及格线：</span>
        <template>
          <el-input-number v-model="threshold"
                           :min="0"
                           :max="10"
                           placeholder="及格线"></el-input-number>
        </template>
      </el-col>

      <el-col :span="24"
              style="margin-top: 20px">
        <el-button @click="createSet"
                   type="primary"
                   style="width: 150px; margin-left: 90px">
          创建
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import request from '@/api/request'

export default {
  data() {
    return {
      setLevel: null,
      threshold: null,
    }
  },

  methods: {
    createSet() {
      //创建套题
      request({
        url: '/createSet',
        method: 'post',
        data: {
          setLevel: this.setLevel,
          threshold: this.threshold,
          teacherId: sessionStorage.getItem('token'),
        },
      }).then((res) => {
        console.log(res)
        this.$message({
          type: 'success',
          message: '创建套题成功！',
        })
        this.$router.push({
          name: 'CreateTest',
        })
      })
    },
  },
}
</script>

<style scoped>
.el-row {
  width: 600px;

  height: 400px;

  position: relative;

  left: 0;

  right: 0;

  top: 0;

  bottom: 0;

  margin: auto;
}
</style>