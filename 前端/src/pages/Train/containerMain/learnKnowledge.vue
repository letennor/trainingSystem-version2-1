<template>
  <div>
  <el-breadcrumb separator-class="el-icon-arrow-right">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item
    >训练系统</el-breadcrumb-item
    >
    <el-breadcrumb-item>学习知识点</el-breadcrumb-item>
  </el-breadcrumb>
    <el-input
        placeholder="输入关键字进行过滤"
        style="margin-top: 10px"

        v-model="filterText">
    </el-input>

      <el-tree
          class="filter-tree"
          style="margin-top: 10px"
          :data="data"
          :props="defaultProps"
          default-expand-all
          @node-click="getChoose"
          :filter-node-method="filterNode"
          ref="tree">
      </el-tree>

  </div>
</template>

<script>
import request from "@/api/request";
export default {
  name: "learnKnowledge",
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },

  },
  data() {
    return {
      filterText: '',
      // point1:[],//编程语言基础的知识点
      // point2:[],//数据结构的知识点
      // point3:[],//算法的知识点
      data: [{
        id: 1,
        label: '编程语言基础',
        disabled: true,
        children: []
      },
        {
        id: 2,
        label: '数据结构',
        children: []
      },
        {
          id: 3,
          label: '算法',
          children:[]
        }],
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    };
  },
  methods: {
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    getPoint(){
      request({
        url:"/study",
        method: "GET",
      }).then(res =>{

        for(let i=0; i<res.data.length;i++)
        {
          let p={
            id:res.data[i].knowledgePointId,
            label: res.data[i].knowledgePoint
          };
          if(res.data[i].knowledgeType=="编程语言基础"){
            // this.point1.push(p)
            this.data[0].children.push(p)
          }
          if(res.data[i].knowledgeType=="数据结构"){
            // this.point2.push(p)
            this.data[1].children.push(p)
          }
          if(res.data[i].knowledgeType=="算法"){
            // this.point3.push(p)
            this.data[2].children.push(p)
          }
        }
      })
    },
    getChoose(data){
      if(data.id!==1&&data.id!==2&&data.id!==3)
      this.$router.push({
        path:"/learning",
        query: {knowledgePointId:data.id}

      })
    }
  },
  created() {
    this.getPoint()
  }


}
</script>

<style>
.el-tree-node{
  position: relative;
  padding-left: 16px;
}
.el-tree-node__children{
  padding-left: 16px;
}
.el-tree-node :last-child:before {
  height: 38px;
}
.el-tree>.el-tree-node:before{
  border-left: none;
}
.el-tree>.el-tree-node:after{
  border-top: none;
}
.el-tree-node:before,.el-tree-node:after{
  content: "";
  left: -4px;
  position: absolute;
  right: auto;
  border-width: 1px;
}
.tree :first-child .el-tree-node:before{
  border-left: none;
}
.el-tree-node:before {
  border-left: 1px dashed #4386c6;
  bottom: 0px;
  height: 100%;
  top: -20px;
  width: 1px;
}
.el-tree-node:after {
  border-top: 1px dashed #4386c6;
  height: 20px;
  top: 17px;
  width: 24px;
}
::v-deep.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {

  background-color: #156cc2;
}
 .el-tree-node__content:hover {

  background-color: #f5da8a !important;

}
</style>
