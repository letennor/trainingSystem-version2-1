<template>
  <div>
    <el-row :gutter="40"
            style="height: 700px">
      <el-col :span="12">
        <el-card shadow="hover"
                 @click.native="createSpecialTest"
                 style="font-size:30px;">
          制作专项试卷
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover"
                 @click.native="createSet"
                 style="font-size:30px">
          {{setMsg}}
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>


<script>
import request from '@/api/request'

export default {
  name: 'CreateTest',
  data() {
    return {
      setTestCode: null,
      setMsg: '',
      setId: null,
      normalTestNumber: null,
    }
  },

  methods: {
    createSpecialTest() {
      this.$router.push({
        name: 'SpecialTest',
      })
    },

    createSet() {
      //判断要去到哪里
      /**
       * 1. code:2    直接跳转到制作试卷页面
       * 2. code:1/3  跳转到制作套题页面
       */
      if (this.setTestCode === 2) {
        this.$router.push({
          name: 'NormalTest',
          params: {
            normalTestNumber: this.normalTestNumber,
            setId: this.setId,
          },
        })
      }

      if (this.setTestCode === 1 || this.setTestCode === 3) {
        this.$router.push({
          name: 'SetTest',
        })
      }
    },
  },

  mounted() {
    //请求这个老师制作套题的信息
    request({
      url: '/queryTeacherSetInfo',
      method: 'post',
      data: {
        teacherId: sessionStorage.getItem('token'),
      },
    }).then((res) => {
      this.setTestCode = res.code
      console.log('传过来的setId', res.data.setId)
      this.setId = res.data.setId
      this.normalTestNumber = res.data.normalTestNumber

      if (this.setTestCode === 2) {
        this.setMsg = `您目前正在制作套题，进度为${this.normalTestNumber} / 10`
      }

      if (this.setTestCode === 3 || this.setTestCode == 1) {
        this.setMsg = '您目前没有正在制作的套题'
      }
    })
  },
}
</script>

<style scoped>
.el-card {
  height: 300px;
  text-align: center;
  line-height: 280px;
}
</style>