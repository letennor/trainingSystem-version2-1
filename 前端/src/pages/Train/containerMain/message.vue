<template>
<div>
  <el-breadcrumb separator-class="el-icon-arrow-right">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item
    >训练系统</el-breadcrumb-item
    >
    <el-breadcrumb-item>留言管理</el-breadcrumb-item>
  </el-breadcrumb>
      <el-descriptions class="margin-top" :column="1" border>
        <template slot="extra">
          <el-button type="primary" icon="el-icon-message" @click="openDialogMessage"
          >发表留言</el-button>
        </template>
      </el-descriptions>
      <el-table style="width: 100%;"
                :data="messages.slice((currentPage-1)*pageSize,currentPage*pageSize)"
                :stripe="true" :border="true"
                :default-sort = "{prop: 'gmtCreate', order: 'descending'}"
               >
       <el-table-column type="index" >
       </el-table-column>
        <el-table-column label="标题" prop="messageTitle" width="150"
                         :show-overflow-tooltip="true">
       </el-table-column>
       <el-table-column label="留言内容" prop="messageContent"  width="180"
                        :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="教师" prop="teacherName" width="100">
        </el-table-column>
        <el-table-column label="回复内容" prop="messageReply" width="180"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="时间" prop="gmtCreate"
                         type="date"
                         value-format="yyyy-MM-dd"
                         sortable>
          <template slot-scope="scope">{{scope.row.gmtCreate |formatDate}}</template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <!-- 查看按钮 -->
            <el-tooltip  effect="dark" content="查看" placement="top" :enterable="false">
              <el-button type="success" icon="el-icon-view" size="mini"
                         @click="seeMessage(scope.row)"
                             ></el-button>
            </el-tooltip>
            <!-- 修改按钮 -->
            <el-tooltip  effect="dark" content="修改" placement="top"
                         :enterable="false"
            >
            <el-button type="primary" icon="el-icon-edit" size="mini"
                         :disabled="scope.row.isReplied!==0"
                         @click="modify(scope.row)"
            ></el-button>
            </el-tooltip>
            <!-- 删除按钮 -->
            <el-tooltip  effect="dark" content="删除" placement="top" :enterable="false">
            <el-button type="danger" icon="el-icon-delete" size="mini"
                        :disabled="scope.row.isReplied!==0"
                       @click="deletedMessage(scope.row)"
            ></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          v-show="messages.length!==0"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20, 40]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="messages.length">
      </el-pagination>

  <el-dialog title="留言" :visible.sync="dialogSubmitMessage"
             :append-to-body="true" @close="closeDialogMessage()">
    <el-form :model="message" ref="message" :rules="rules1">
      <el-form-item label="教师" label-width="120px" prop="teacherId">
        <el-select v-model="message.teacherId" placeholder="请选择提问的教师" >
          <el-option
              v-for="(teacher,index) in teachers" :label="teacher.name" :value="teacher.userId"
                          :key="index"></el-option>
<!--          <el-option label="模式一" value="模式一"></el-option>-->
<!--          <el-option label="模式二" value="模式二"></el-option>-->
<!--          <el-option label="模式三" value="模式三"></el-option>-->
        </el-select>
      </el-form-item>
      <el-form-item label="留言标题" label-width="120px" prop="title">
        <el-input type="text" v-model="message.title"
                  placeholder="请输入标题"
                  autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item label="留言内容" label-width="120px" prop="content">
        <el-input type="textarea" v-model="message.content"
                  placeholder="请输入要提问的内容"
                  autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialogMessage()">取 消</el-button>
      <el-button  v-if="openflag1===1" type="primary" @click="sendMessage('message')">确 定</el-button>
      <el-button  v-if="openflag1===2" type="primary" @click="modifyMessage('message')">确 定</el-button>
    </div>
  </el-dialog>
  <el-dialog title="留言" :visible.sync="dialogSeeMessage"
             :append-to-body="true">
    <el-descriptions class="margin-top"  :column="1"  border>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>
          教师
        </template>
       {{ message1.teacherName}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-opportunity"></i>
          标题
        </template>
        {{ message1.messageTitle}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-message"></i>
          留言内容
        </template>
        {{ message1.messageContent}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-date"></i>
          留言时间
        </template>
        {{ message1.gmtCreate}}
      </el-descriptions-item>

      <el-descriptions-item v-if="message1.isReplied===0">
        <template slot="label">
          <i class="el-icon-s-claim"></i>
          教师回复内容
        </template>
        尚未回复，请等待教师的回复
      </el-descriptions-item>
      <el-descriptions-item v-if="message1.isReplied===1">
        <template slot="label">
          <i class="el-icon-s-claim"></i>
          教师回复内容
        </template>
        {{ message1.messageReply}}
      </el-descriptions-item>
      <el-descriptions-item v-if="message1.isReplied===1">
        <template slot="label">
          <i class="el-icon-date"></i>
          回复时间
        </template>
        {{ message1.replyTime}}
      </el-descriptions-item>
    </el-descriptions>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogSeeMessage=false">关闭</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
import request from "@/api/request";
var padDate = function (value) {
  return value < 10 ? '0' + value : value;
}
export default {
  name: "message",
  data(){
    return{
      dialogSubmitMessage:false,//是否打开发表留言的弹窗
      dialogSeeMessage:false,//是否查看留言的弹窗
      openflag1:1, //1表示发表，2表示修改,
      currentPage:1, //初始页
      pageSize:5,    // 每页的数据
      teachers:[] ,//老师信息
       messages:[],//所有留言
      message:{//一条留言
        messageId:'',//留言Id
        teacherId:'',//被提问的教师Id
        title:'',//留言标题
        content:'',//留言内容
      } ,
      message1:{},//查看的留言信息
      rules1:{
        teacherId: [{required: true, message: '请选择教师', trigger: 'blur' }],
        title: [{required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{required: true, message: '请输入留言', trigger: 'blur' }],
      }
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
  methods:{
    //关闭发表留言的弹窗
    closeDialogMessage(){
      this.dialogSubmitMessage=false
      this.message.title=''
      this.message.content=''
      this.message.teacherId=''

    },
    //发表留言的按钮
    openDialogMessage(){
      this.openflag1=1;
      this.dialogSubmitMessage=true
      this.getTeacher()
    },
    //发表留言的请求
    sendMessage(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          request({
            url:"/student/submitMessage",
            method:"POST",
            data:{
              userId:sessionStorage.getItem("token"),
              teacherId:this.message.teacherId,
              title:this.message.title,
              content:this.message.content

            }
          }).then(res =>{
            this.$message(res.msg)
            this.dialogSubmitMessage=false
            this.getMessages()
          })
        }
      })
    },
   //获取留言信息
    getMessages(){
      request({
        url:"/student/allMessage",
        method:"POST",
        data:{
          userId:sessionStorage.getItem("token"),
        }

      }).then(res =>{
        this.messages=res.data
      })
    },
    //获取教师信息
    getTeacher(){
      request({
        url:"/allTeacher",
        method:"GET"
      }).then(res =>{
        this.teachers=res.data
      })
    },
    //修改留言的请求
    modifyMessage(formName){
      this.$refs[formName].validate((valid) => {
            if (valid) {
              request({
                url:"/student/modifyMessage",
                method:"POST",
                data:{
                  messageId:this.message.messageId,
                  teacherId:this.message.teacherId,
                  title:this.message.title,
                  content:this.message.content
                }
              }).then(res =>{
                this.$message(res.msg)
                this.dialogSubmitMessage=false
                this.getMessages()
              })
            }
    })},
    //修改的按钮
    modify(data) {
      this.openflag1=2;
      this.message.messageId=data.messageId;
      this.message.title=data.messageTitle;
      this.message.teacherId=data.teacherId;
      this.message.content=data.messageContent
      this.dialogSubmitMessage=true
      this.getTeacher()

    },
    //删除留言请求
    deletedMessage(data){
      this.$confirm('此操作将永久删除该留言, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
      request({
        url:"/student/deleteMessage",
        method:"POST",
        data:{
          messageId:data.messageId
        }
      }).then(res =>{
        this.$message(res.msg)
        this.getMessages()
      })
      })
    },
    //查看留言
    seeMessage(data){
      this.dialogSeeMessage=true
      this.message1=data
    },

    // 初始页currentPage、初始每页数据数pagesize和数据data
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
   this.getMessages()
  },

}
</script>

<style scoped>


</style>