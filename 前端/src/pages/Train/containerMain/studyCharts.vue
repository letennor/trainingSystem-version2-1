<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right" v-if="userType == 1">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/trainHome' }"
        >训练系统</el-breadcrumb-item
      >
      <el-breadcrumb-item>学习记录</el-breadcrumb-item>
      <el-breadcrumb-item>学习统计</el-breadcrumb-item>
    </el-breadcrumb>
    <p style="font-size: 18px; background: #dbdce7; margin-top: 10px">
      近十次的试题情况
    </p>
    <el-row>
      <div id="myChart5" style="width: 100%; height: 200px"></div>
    </el-row>
    <p style="font-size: 18px; background: #dbdce7">普通练习</p>
    <el-row>
      <el-col :span="12">
        <h2 style="width: 420px">
          <i class="el-icon-edit"></i> 已完成练习<em>{{ normal}}</em>
        </h2>
        <div id="myChart" class="echart"></div>
      </el-col>
      <el-col :span="12">
        <h2 style="width: 420px">
          <i class="el-icon-finished"></i> 通过率<em>{{ pass / 100 + "%" }}</em>
        </h2>
        <div
          id="myChart1"
          style="width: 380px; height: 200px; margin-left: 0px"
        ></div>
      </el-col>
    </el-row>
    <p style="font-size: 18px; background: #dbdce7">专项练习</p>
    <el-row>
      <el-col :span="12">
        <h2 style="width: 420px">
          <i class="el-icon-edit"></i> 已完成练习<em>{{ special }}</em>
        </h2>
        <div
          id="myChart2"
          style="width: 380px; height: 200px; margin-left: 0px"
        ></div>
      </el-col>
      <el-col :span="12">
        <h2 style="width: 420px">
          <i class="el-icon-finished"></i> 通过率<em>{{
            pass1 / 100 + "%"
          }}</em>
        </h2>
        <div
          id="myChart3"
          style="width: 380px; height: 200px; margin-left: 0px"
        ></div>
      </el-col>
    </el-row>
    <div v-show="point.length!==0">
      <p style="font-size: 18px; background: #dbdce7">知识点分析</p>
      <div
        id="myChart4"
        style="width: 800px; height: 600px; margin-left: 0px"
      ></div>
    </div>
  </div>
</template>

<script>
import request from "@/api/request";

var echarts = require("echarts");
export default {
  name: "StudyCharts",

  data() {
    return {
      sum1question: 0, //普通练习中对题的个数
      sum1: 0, //普通练习的题目个数
      sum2: 0, //专项练习的题目个数
      sum2question: 0, //专项练习中对题的个数
      //  score1:[0,0,0],//普通练习每张对的个数所在区间
      // score2:[0,0]
      normal:0,//普通练习的个数
      special:0,//专项练习的个数
      passnum1: 0, //普通练习通过
      passnum2: 0, //专项练习通过
      point: [], //知识点
      p1: [], //
      p2: [],
    };
  },
  mounted() {
    this.optionscore(),
      this.optionscore1(),
      this.optionscore2(),
      this.optionscore3(),
      this.optionscore4(),
      this.optionscore5();
  },
  methods: {
    optionscore() {
      //普通1
      let myChart = echarts.init(document.getElementById("myChart")); // 初始化echarts, theme为light
      let echartsOption = {
        // echarts选项，所有绘图数据和样式都在这里设置
        title: {
          text: this.sum1question + "/" + this.sum1,
          left: "center",
          top: "center",
        },
        tooltip: {
          //鼠标放到图上的数据展示样式
          trigger: "item",
          formatter: "{b}:{c}({d}%)",
        },
        series: [
          {
            type: "pie",
            data: [
              {
                value: this.sum1question,
                name: "做对的题",
              },
              {
                value: this.sum1 - this.sum1question,
                name: "做错的题",
              },
            ],
            radius: ["40%", "70%"],
          },
        ],
      };
      myChart.setOption(echartsOption); // echarts设置选项
      window.onresize = function () {
        myChart.resize();
      };
    },
    optionscore2() {
      //专项1
      let myChart = echarts.init(document.getElementById("myChart2"), "light"); // 初始化echarts, theme为light
      let echartsOption = {
        // echarts选项，所有绘图数据和样式都在这里设置
        title: {
          text: this.sum2question + "/" + this.sum2,
          left: "center",
          top: "center",
        },
        tooltip: {
          //普通2
          trigger: "item",
          formatter: "{b}:{c}({d}%)",
        },
        series: [
          {
            type: "pie",
            data: [
              {
                value: this.sum2question,
                name: "做对的题",
              },
              {
                value: this.sum2 - this.sum2question,
                name: "做错的题",
              },
            ],
            radius: ["40%", "70%"],
          },
        ],
      };
      myChart.setOption(echartsOption); // echarts设置选项
      window.onresize = function () {
        myChart.resize();
      };
    },
    optionscore1() {
      let myChart = echarts.init(document.getElementById("myChart1"));
      let option1 = {
        title: {
          text: this.passnum1 + "/" + this.normal,
          left: "center",
          top: "center",
        },
        tooltip: {
          //鼠标放到图上的数据展示样式
          trigger: "item",
          formatter: "{b}:{c}({d}%)",
        },
        series: [
          {
            type: "pie",
            data: [
              {
                value: this.passnum1,
                name: "通过的试题",
              },
              {
                value: this.normal - this.passnum1,
                name: "未通过试题",
              },
            ],
            radius: ["40%", "70%"],
          },
        ],
      };
      myChart.setOption(option1); // echarts设置选项
      window.onresize = function () {
        myChart.resize();
      };
    },
    optionscore3() {
      //专项2
      let myChart = echarts.init(document.getElementById("myChart3"), "light"); // 初始化echarts, theme为light
      let echartsOption = {
        // echarts选项，所有绘图数据和样式都在这里设置
        title: {
          text: this.passnum2 + "/" + this.special,
          left: "center",
          top: "center",
        },
        tooltip: {
          //普通2
          trigger: "item",
          formatter: "{b}:{c}({d}%)",
        },
        series: [
          {
            type: "pie",
            data: [
              {
                value: this.passnum2,
                name: "通过的试题",
              },
              {
                value: this.special - this.passnum2,
                name: "未通过的试题",
              },
            ],
            radius: ["40%", "70%"],
          },
        ],
      };
      myChart.setOption(echartsOption); // echarts设置选项
      window.onresize = function () {
        myChart.resize();
      };
    },
    optionscore4() {
      //专项2
      let myChart = echarts.init(document.getElementById("myChart4"), "light"); // 初始化echarts, theme为light
      let echartsOption = {
        // echarts选项，所有绘图数据和样式都在这里设置
        title: {
          text: "已学知识点的次数",
          left: "center",
          top: 10,
          textStyle: {
            // color: '#ccc'
          },
        },
        tooltip: {
          trigger: "item",
        },
        // visualMap: {
        //   show: false,
        //   min: this.point.min,
        //   max: this.point.max,
        //   inRange: {
        //     colorLightness: [0, 1]
        //   }
        // },
        series: [
          {
            name: "Knowledge",
            type: "pie",
            radius: "65%",
            center: ["50%", "50%"],
            data: this.point,
            //     .sort(function (a, b) {
            //   return a.value - b.value;
            // }),
            roseType: "radius",
            label: {
              color: "rgba(12,1,1,0.93)",
            },
            labelLine: {
              lineStyle: {
                color: "rgba(3,0,0,0.96)",
              },
              smooth: 0.2,
              length: 10,
              length2: 20,
            },
            // itemStyle: {
            //   color: '#a8d5ea',
            // //  shadowBlur: 200,
            //  // shadowColor: 'rgba(0, 0, 0, 0.5)'
            // },
            animationType: "scale",
            animationEasing: "elasticOut",
            // animationDelay: function (idx) {
            //   return Math.random() * 200;
            // }
          },
        ],
      };
      myChart.setOption(echartsOption); // echarts设置选项
      window.onresize = function () {
        myChart.resize();
      };
    },
    optionscore5() {
      //专项2
      let myChart = echarts.init(document.getElementById("myChart5"), "light"); // 初始化echarts, theme为light
      let echartsOption = {
        // echarts选项，所有绘图数据和样式都在这里设置
        title: {
          //text: '做题情况'
        },
        tooltip: {
          trigger: "axis",
        },
        legend: {
          data: ["普通练习", "专项练习"],
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
          data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: "普通练习",
            type: "line",
            // stack: "Total",
            data: this.p1,
          },
          {
            name: "专项练习",
            type: "line",

            data: this.p2,
          },
        ],
      };
      myChart.setOption(echartsOption); // echarts设置选项
      window.onresize = function () {
        myChart.resize();
      };
    },
    setscore(data) {
      //获取对错数据
      let n1 = [];
      let n2 = [];
      for (let i = 0; i < data.length; i++) {
        if (data[i].testType == 1) {
          //普通练习
          n1.push(data[i]);
          this.sum1question += data[i].score;
          this.normal+=1;
          this.sum1 +=  data[i].questionNumber;
          if (data[i].status == 1) {
            //通过
            this.passnum1 += 1;
          }
        }
        if (data[i].testType == 0) {
          //专项练习
          n2.push(data[i]);
          this.sum2 += data[i].questionNumber;
          this.special+=1;
          this.sum2question += data[i].score;
          if (data[i].status == 1) {
            //通过
            this.passnum2 += 1;
          }
        }
      }
      this.setdate(n1);
      this.setdate(n2);
      var p=[];
      for (let i = 0; i < n1.length; i++) {
        if (i < 10) {
          p.push(n1[i].score);

        }
      }
      for(let i=p.length-1;i>=0;i--){
        this.p1.push(p[i]);
      }
      p=[];
      for (let i = 0; i < n2.length; i++) {
        if (i < 10) {
          p.push(n2[i].score);
        }
      }
      for(let i=p.length-1;i>=0;i--){
        this.p2.push(p[i]);
      }

      // console.log(this.p1);
      // console.log(this.p2);
    },
    setpoint(data) {
      this.point.length = 0;
      for (let i = 0; i < data.length; i++) {
        let p1 = {
          value: 0,
          name: "",
        };
        p1.value = data[i].count;
        // p1.value=i+1;
        p1.name = data[i].knowledgePoint;
        this.point.push(p1);
      }
     // console.log(this.point);
    },
    setdate(data) {
      //date sort
      data.sort(function (a, b) {
        return a.gmtCreate < b.gmtCreate ? 1 : -1;
      });
    },
    getrecode() {
      //

      request({
        url: "/student/record",
        method: "POST",
        data: {
          userId: this.userId,
        },
      }).then((res) => {
        this.setscore(res.data);
      });
    },
    getpoint() {
      request({
        url: "/student/record/point",
        method: "POST",
        data: {
          userId: this.userId,
        },
      }).then((res) => {
        this.setpoint(res.data);
      });
    },
  },
  computed: {
    pass() {
      if(this.normal!==0)
      {
        return parseInt( parseFloat(this.passnum1 / this.normal)* 10000);
      }
      else {
        return 0;
      }

    },
    pass1() {
      if(this.special!==0)
      {
        return parseInt( parseFloat(this.passnum2 / this.special)*10000);
      }
      else {
        return 0;
      }

    },
    // 学生id
    userId() {
      return this.$route.params.userId ? this.$route.params.userId : sessionStorage.getItem('token');
    },
    userType() {
      return sessionStorage.getItem('userType')
    }
  },

  created() {
    this.getrecode();
    this.getpoint();
  },
  watch: {
    sum1: {
      handler() {
        this.optionscore();

      },
      deep: true,
    },
    normal: {
      handler() {

        this.optionscore1();
      },
      deep: true,
    },
    special: {
      handler() {
        this.optionscore2();
        this.optionscore3();
      },
      deep: true,
    },
    sum2: {
      handler() {
        this.optionscore2();
        this.optionscore3();
      },
      deep: true,
    },
    sum1question: {
      handler() {
        this.optionscore();
      },
      deep: true,
    },
    sum2question: {
      handler() {
        this.optionscore2();
      },
      deep: true,
    },
    passnum1: {
      handler() {
        this.optionscore1();
      },
      deep: true,
    },
    passnum2: {
      handler() {
        this.optionscore3();
      },
      deep: true,
    },
    point: {
      handler() {
        this.optionscore4();
      },
      deep: true,
    },
    p1: {
      handler() {
        this.optionscore5();
      },
      deep: true,
    },
    p2: {
      handler() {
        this.optionscore5();
      },
      deep: true,
    },
  },
};
</script>

<style scoped>
#myChart {
  width: 380px;
  height: 200px;
  margin-left: 0px;
}
h2 em {
  color: #7abaff;
  font-size: 40px;
  margin-left: 20px;
}
</style>
