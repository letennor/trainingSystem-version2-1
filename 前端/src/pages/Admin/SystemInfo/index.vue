<template>
  <div>
    <el-row :gutter="40">
      <el-col :span="16" :offset="1">
        <el-card>
          <div
            ref="lastWeekUserNumberCharts"
            class="lastWeekUserNumberCharts"
          ></div>
        </el-card>
      </el-col>
      <el-col :span="6" :offset="1">
        <!-- <el-card class="card-users-data">
          <p class="card-title">总用户量</p>
          <p style="font-size: 30px">666</p>
        </el-card> -->
        <el-card class="card-users-data card-online-users">
          <span class="card-online-title">在线用户量</span>
          <span style="display: block;font-size: 45px;">{{onlineUserNumber}}</span>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from "@/api/request";
var echarts = require("echarts");
export default {
  name: "SystemInfo",
  data() {
    return {
      lastWeekUserNumber: [],// 最近七天的用户量
      onlineUserNumber: 0,
    };
  },
  computed: {
    lastSevenDays() {
      let res = [];
      for (let i = 7; i > 0; i--) {
        res.push(this.getDay(-i));
        // console.log(res);
      }
      return res;
    }
  },
  methods: {
    getLastWeekUserNumber() {
      request({
        url: "/getLastWeekUserNumber",
        method: "GET",
      }).then((res) => {
        if (res.code == 200) {
          console.log(res);
          this.lastWeekUserNumber = res.data.reverse();
          this.showEcharts();
        }
      });
    },
    showEcharts() {
      // console.log(this.lastSevenDays);
      let testEcharts = echarts.init(this.$refs.lastWeekUserNumberCharts);
      let echartsOptions = {
        title: {
          text: "近一周用户量统计",
        },
        tooltip: {
          trigger: "axis",
        },
        legend: {
          data: ["用户量"],
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          // data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
          data: this.lastSevenDays,
        },
        yAxis: {
          type: "value",
          minInterval: 1,
          axisLabel: {
            formatter: '{value}人'
          }
        },
        series: [
          {
            name: "用户量",
            type: "line",
            data: this.lastWeekUserNumber,
          },
        ],
      };
      testEcharts.setOption(echartsOptions);
    },
    // 计算日期
    getDay(day) {
      var today = new Date();

      var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;

      today.setTime(targetday_milliseconds); //注意，这行是关键代码

      var tMonth = today.getMonth() + 1;
      var tDate = today.getDate();
      tMonth = tMonth < 10 ? "0" + tMonth : tMonth;
      tDate = tDate < 10 ? "0" + tDate : tDate;
      // console.log(tMonth + "-" + tDate);
      return tMonth + "-" + tDate;
    },
    getOnlineUserNumber() {
      request({
        url: '/getOnlineUserNumber',
        method: 'GET',
      }).then(res => {
        console.log(res);
        if (res.code == 200) {
          this.onlineUserNumber = res.data;
        }
      })
    }
  },
  created() {
    console.log("created");
    this.getLastWeekUserNumber();
    this.getOnlineUserNumber();
  },
  mounted() {
    console.log("mounted");
  },
};
</script>

<style scoped>
.lastWeekUserNumberCharts {
  width: 800px;
  height: 450px;
}
.card-users-data {
  /* float: left; */
  width: 200px;
  height: 200px;
  border-radius: 50%;
  text-align: center;
}
.card-online-users {
  margin-top: 20px;
}
.card-online-title {
  display: block;
  margin-top: 10px;
  margin-bottom: 20px;
  font-size: 20px;
}
</style>
