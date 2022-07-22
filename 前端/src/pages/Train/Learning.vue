<template>
  <div>
    <div class="header-bar">
      <div class="header-left">
        <div class="question-title">
          <i class="el-icon-s-flag"></i
          >学习知识点
        </div>
      </div>
      <div class="header-right">
        <ul class="code-list-box">
          <li>
            <router-link to="/trainHome/learnKnowledge">
              <i class="el-icon-s-home"></i>
              学习主页
            </router-link>
          </li>
        </ul>
        <a href="" class="head-pic"> </a>
      </div>
    </div>
    
    <div class="point-page">
        <iframe
          :src="url"
          frameborder="0"
          style="width: 100%; height: 100%"
        ></iframe>
      </div>
    <!-- <iframe :src="url" frameborder="0" style="width: 100%; height: 500px"></iframe> -->
  </div>
</template>

<script>
import request from "@/api/request";
export default {
  name: "Learning",
  data(){
    return{
      url:'',//知识点的URL
      name:'',//知识点名称
      // url1: "https://www.runoob.com/",
    }

  },
  methods:{
    geturl(){
      request({
        url:'/tolearn',
        method:"POST",
        data:{
          userId: window.sessionStorage.getItem("token"),
          knowledgePointId: this.$route.query.knowledgePointId
        }
      }).then(res =>{
        // console.log(res.data)
        this.name=res.data.knowledgePoint
        this.url=res.data.url
      })
    }
  },
  created() {
    this.geturl()
  }
}
</script>

<style scoped>
.point-page {
  position: relative;
  width: 85%;
  height: 90vh;
  margin: auto;
}
/* header */
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