<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item
        >训练系统</el-breadcrumb-item
      >
      <el-breadcrumb-item>学习记录</el-breadcrumb-item>
      <el-breadcrumb-item>练习记录</el-breadcrumb-item>
    </el-breadcrumb>
<!--    <div style="margin-top: 20px">-->
<!--      <el-row :gutter="40" v-for="(item,key) in test" :key="key">-->
<!--      <el-col :span="22" >-->
<!--        <el-card shadow="hover">-->
<!--          <el-col :span="20" >-->
<!--          {{item.testName}}-->
<!--          </el-col>-->
<!--          <el-col :span="2" >-->
<!--          <el-tag >{{ item.score }}</el-tag>-->
<!--          </el-col>-->
<!--       </el-card>-->
<!--      </el-col>-->
<!--      </el-row>-->
<!--    </div>-->
    <div style="margin-top: 20px">
    <el-table :data="test.slice((currentPage-1)*pageSize,currentPage*pageSize)"
              :default-sort = "{prop: 'gmtCreate', order: 'descending'}"
      border
      style="width: 100%"
      @row-click="rowClick">
       <el-table-column
          prop="testName"
          label="练习名称"
       >
       </el-table-column>
      <el-table-column
        prop="score"
        label="做对的题个数"
      ></el-table-column>

      <el-table-column
         prop="gmtCreate"
         type="date"
         value-format="yyyy-MM-dd"
         sortable
         label="完成日期">
        <template slot-scope="scope">{{scope.row.gmtCreate |formatDate}}</template>
      </el-table-column>
    </el-table>
      <el-pagination
          v-show="test.length!==0"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20, 40]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="test.length">
      </el-pagination>
  </div>
  </div>
</template>

<script>
import request from "@/api/request";
var padDate = function (value) {
  return value < 10 ? '0' + value : value;
}
export default {
  name: "Practice",

  data()
  {
    return{
      test:[],
      count:0,
      currentPage:1, //初始页
      pageSize:10,    // 每页的数据
    }
  },
  filters: {
    formatDate: function (value) {
      var date = new Date(value);
      var year = date.getFullYear();
      var month = padDate(date.getMonth() + 1);
      var day = padDate(date.getDate());
      var hours = padDate(date.getHours());
      var minutes = padDate(date.getMinutes());
      var seconds = padDate(date.getSeconds());
      //返回数据
      return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
    }
  },
  methods: {
    gettest() {
      request({
        url: '/student/record',
        method: 'POST',
        data: {
          userId: sessionStorage.getItem('token')
        }
      }).then(res => {
        // console.log(res);
        this.test = res.data;
      })

    },
    load() {
      this.count += 2
    },
   rowClick(row){
      // console.log(row.testId)
      this.$router.push({path:'/viewRecord',query: { testId :row.testId, Q: 0}})
   },
    handleSizeChange: function (size) {
      this.pageSize = size;
      console.log(this.pageSize)  //每页下拉显示数据
    },
    handleCurrentChange: function(currentPage){
      this.currentPage = currentPage;
      console.log(this.currentPage)  //点击第几页
    },
  },
  created() {
    this.gettest()
  },


};
</script>

<style scoped>
.el-row {
  margin-bottom: 10px;
}
</style>
