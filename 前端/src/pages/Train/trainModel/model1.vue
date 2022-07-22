<template>
  <div>
    <div class="header-bar">
      <div class="header-left">
        <div class="question-title"><i class="el-icon-s-flag"></i>模式一</div>
      </div>
      <div class="header-right">
        <ul class="code-list-box">
          <li>
            <el-button
            style="color: #ffffff"
              type="text"
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
              "
            >
              <i class="el-icon-s-home"></i>
              训练主页
            </el-button>
          </li>
        </ul>
        <a href="" class="head-pic"> </a>
      </div>
    </div>
    <el-card class="box-card">
      <div class="point-page">
        <iframe
          :src="url"
          frameborder="0"
          style="width: 100%; height: 100%"
        ></iframe>
        <el-button type="primary" class="btn-complete" @click="toPractice">前去练习</el-button>
      </div>
      <!-- <div style="text-align: center">
        <label
          ><h3>
            <input
              type="checkbox"
              v-model="flag"
              style="vertical-align: middle"
            />已完成学习
          </h3></label
        >
      </div>
      <div style="text-align: center">
        <el-button
          type="primary"
          :disabled="!flag"
          @click="$router.push('/modelQuestion1?Q=0')"
          >前去练习</el-button
        >
      </div> -->
    </el-card>
  </div>
</template>

<script>
import request from "@/api/request";

export default {
  name: "Model1",
  data() {
    return {
      name: "", //知识点名称
      url: "", //知识点url
      flag: 0, //0表示未学习，1表示已学习
      // url1: "https://www.runoob.com/",
    };
  },
  methods: {
    getknow() {
      request({
        url: "/model1/study",
        method: "post",
        data: {
          userId: sessionStorage.getItem("token"),
        },
      }).then((res) => {
        console.log(res);
        this.name = res.data.knowledge;
        this.url = res.data.url;
      });
    },
    toPractice() {
      this.$confirm('请掌握知识点后再去练习，是否确认前往？', '提示',{
        confirmButtonText: '确定',
          cancelButtonText: '再看一会',
          type: 'warning'
      }).then(()=> {
        this.$router.replace('/modelQuestion1?Q=0');
      }).catch(()=> {
        console.log('取消前往');
      })
    }
  },
  created() {
    this.getknow();
  },
};
</script>

<style scoped>
.point-page {
  position: relative;
  width: 85%;
  height: 90vh;
  margin: auto;
}
.btn-complete {
  position: absolute;
  top: 20px;
  right: -110px;
}
.header-bar {
  background: #232c31;
  color: #fff;
  line-height: 50px;
  font-size: 14px;
  overflow: hidden;
  height: 9vh;
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
</style>
