<template>
  <div>
    <div v-clickoutside="hideReplyBtn" @click="inputFocus" class="my-reply">
      <el-avatar class="header-img" :size="40" >{{username.charAt(0)}}</el-avatar>
      <div class="reply-info" >
        <div
            tabindex="0"
            contenteditable="true"
            id="replyInput"
            spellcheck="false"
            placeholder="输入评论..."
            class="reply-input"
            @focus="showReplyBtn"
            @input="onDivInput($event)"
        >
        </div>
        <div class="reply-btn-box" v-show="btnShow">
          <el-button class="reply-btn" size="medium" @click="sendComment" type="primary">发表评论</el-button>
        </div>
      </div>
    </div>
    <div v-for="(item,i) in comments.slice((currentPage-1)*pageSize,currentPage*pageSize)" :key="i" class="author-title reply-father">
      <el-avatar class="header-img" :size="40" >{{item.userName.charAt(0)}}</el-avatar>
      <div class="author-info">
            <span class="author-name">{{item.userName}}</span>
            <span class="author-time" >{{ item.gmtCreate | formatDate}}</span>
      </div>
      <div class="icon-btn">
        <el-button type="text"
                   @click="showReplyInput(i,-1,item.commentId,item.userId)">
              <span >
                <i class="iconfont el-icon-s-comment"></i>回复</span>
        </el-button>
        <el-button type="text"
                  v-show="userId==item.userId"
                   @click="deletedcomment(item.commentId)"
        >
              <span >
                <i class="iconfont el-icon-delete"></i>删除</span>
        </el-button>
      </div>
      <div class="talk-box">
        <p>
          <span class="reply">{{item.comment}}</span>
        </p>
      </div>
      <div  v-show="_inputShow(i,-1)" class="my-reply my-comment-reply">
        <el-avatar class="header-img" :size="40">{{username.charAt(0)}}</el-avatar>
        <div class="reply-info" >
          <div tabindex="0" contenteditable="true" spellcheck="false" placeholder="输入评论..."   @input="onDivInput($event)"  class="reply-input reply-comment-input"></div>
        </div>
        <div class=" reply-btn-box">
          <el-button class="reply-btn" size="medium" @click="sendCommentReply(i,j)" type="primary">发表评论</el-button>
        </div>
      </div>
      <div class="reply-box">
        <div v-for="(reply,j) in item.sonComments" :key="j" class="author-title">
          <el-avatar class="header-img" :size="40">{{reply.userName.charAt(0)}}</el-avatar>
          <div class="author-info">
            <span class="author-name">{{reply.userName}}</span>
            <span class="author-time">{{reply.gmtCreate | formatDate}}</span>
          </div>
          <div class="icon-btn">
            <el-button type="text"
                       @click="showReplyInput(i,j,reply.fatherCommentId,reply.userId)">
              <span >
                <i class="iconfont el-icon-s-comment"></i>回复</span>
            </el-button>
            <el-button type="text"
                       v-show="userId==reply.userId"
                       @click="deletedcomment(reply.commentId)"
            >
              <span >
                <i class="iconfont el-icon-delete"></i>删除</span>
            </el-button>
          </div>
          <div class="talk-box">
            <p>
              <span>回复 {{reply.upUserName}}:</span>
              <span class="reply">{{reply.comment}}</span>
            </p>
          </div>
          <div  v-show="_inputShow(i,j)" class="my-reply my-comment-reply">
            <el-avatar class="header-img" :size="40" >{{username.charAt(0)}}</el-avatar>
            <div class="reply-info" >
              <div tabindex="0" contenteditable="true" spellcheck="false" placeholder="输入评论..."   @input="onDivInput($event)"  class="reply-input reply-comment-input"></div>
            </div>
            <div class=" reply-btn-box">
              <el-button class="reply-btn" size="medium" @click="sendCommentReply(i,j)" type="primary">发表评论</el-button>
            </div>
          </div>
          <div class="reply-box">

          </div>
        </div>
      </div>
     </div>
    <el-pagination
        v-show="comments.length!==0"
        style="margin-top: 10px;text-align: center"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[5, 10, 20, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="comments.length">
    </el-pagination>
  </div>
</template>

<script>
import request from "@/api/request";
const clickoutside = {
  // 初始化指令
  bind(el, binding) {
    function documentHandler(e) {
      // 这里判断点击的元素是否是本身，是本身，则返回
      if (el.contains(e.target)) {
        return false;
      }
      // 判断指令中是否绑定了函数
      if (binding.expression) {
        // 如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
        binding.value(e);
      }
    }
    // 给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
    el.vueClickOutside = documentHandler;
    document.addEventListener('click', documentHandler);
  },
  update() {},
  unbind(el) {
    // 解除事件监听
    document.removeEventListener('click', el.vueClickOutside);
    delete el.vueClickOutside;
  },
};
var padDate = function (value) {
  return value < 10 ? '0' + value : value;
}
export default {
  name:'comment',
  props: ['question'],
  data(){
    return {
      userId:sessionStorage.getItem("token"),
      comments:[],
      btnShow: false,
      replyComment:'',//评论
      showReply:-1,//展示回复的输入框
      showInput:-1,
      fatherId:'',//父评论的id
      replayUserId:"",//回复人的id
      currentPage:1, //初始页
      pageSize:5,    // 每页的数据
      questionId:this.question,
      username:sessionStorage.getItem("name"),
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
  directives: {clickoutside},
  methods: {
    //监听输入框
    inputFocus(){
      var replyInput = document.getElementById('replyInput');
      replyInput.style.padding= "8px 8px"
      replyInput.style.border ="2px solid blue"
      replyInput.focus()
    },
    showReplyBtn(){
      this.btnShow = true
    },
    hideReplyBtn(){
      this.btnShow = false
      // replyInput.style.padding= "10px"
      // replyInput.style.border ="none"
    },
    showReplyInput(i,j,fatherId,replayId){
      this.showInput=i;
      this.showReply=j;
     this.fatherId=fatherId;
     this.replayUserId=replayId;
    },
    _inputShow(i,j){
      if(i==this.showInput && j==this.showReply){
        return true
      }

    },
    //发表留言
    sendComment(){
      if(!this.replyComment){
        this.$message({
          showClose: true,
          type:'warning',
          message:'评论不能为空'
        })
      }else{
        let input =  document.getElementById('replyInput')
       request({
         url:"/sendComment",
         method:"POST",
         data:{
           userId:this.userId,
           questionId:this.questionId,
           comment:this.replyComment
         }
       }).then(res =>{
         if(res.data===1)
         {
           this.$message("发表评论成功")
           this.replyComment = ''
           input.innerHTML = '';
           this.getComments()
         }
         else {
           this.$message(res.msg)
         }

       })


      }
    },
    //发表回复
    sendCommentReply(i){
      if(!this.replyComment){
        this.$message({
          showClose: true,
          type:'warning',
          message:'评论不能为空'
        })
      }else{
        request({
          url:"/replayComment",
          method:"POST",
          data:{
            userId:this.userId,
            questionId:this.questionId,
            fatherCommentId:this.fatherId,
            replytoUserId:this.replayUserId,
            comment:this.replyComment
          }
        }).then(res =>{
          if(res.data===1)
          {
            this.$message("回复评论成功")
            this.showReply=-1;
            this.showInput=-1;
            this.replyComment = ''
            document.getElementsByClassName("reply-comment-input")[i].innerHTML = ""
            this.getComments()
          }
          else {
            this.$message(res.msg)
          }

        } )

      }
    },
    //删除留言
    deletedcomment(commentId){
      this.$confirm('此操作将永久删除该评论, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url:"/deleteComment",
          method:"POST",
          data:{
            commentId:commentId
          }
        }).then(res =>{
          if(res.data===1)
          {
            this.$message("删除成功")
          }
          else {
            this.$message(res.msg)
          }

          this.getComments()
        })
      })
   },
    //获取用户输入的信息
    onDivInput: function(e) {
      this.replyComment = e.target.innerHTML;
    },

    getComments(){
      request({
        url:"/showAllComment",
        method:"POST",
        data:{
          questionId:this.questionId,
        }
      }).then(res =>{
        this.comments=res.data

      })
    },
    // 初始页currentPage、初始每页数据数pagesize和数据data
    handleSizeChange: function (size) {
      this.pageSize = size;
    },
    handleCurrentChange: function(currentPage){
      this.currentPage = currentPage;

    },
  },
  created() {
    this.getComments()
  },
  watch:{
    question(newValue){
      if(newValue){
        this.questionId=newValue
        this.getComments()
      }
    }

  },
}
</script>

<style  scoped>
.my-reply{
  padding: 10px;
  background-color: #fafbfc;
}
  .header-img{
    display: inline-block;
    vertical-align: top
  }
  .reply-info{
    display: inline-block;
    margin-left: 5px;
    width: 90%
  }


.reply-input{
      min-height: 20px;
      line-height: 22px;
      padding: 10px 10px;
      color :#ccc;
      background-color: #fff;
      border-radius: 5px;
    }
.reply-input:empty:before{
  content:"输入评论...";
}
.reply-input:focus:before{
  content: none
}
.reply-input:focus {
  padding: 8px 8px;
  border: 2px solid blue;
  box-shadow :none;
  outline: none;
}
.reply-btn-box{
  height: 25px;
  margin: 10px 0
}
.reply-btn{
  position :relative;
  float: right;
  margin-right: 15px
}
.my-comment-reply{
  margin-left: 50px
}
.author-title:not(:last-child){
  border-bottom: 1px solid rgba(178,186,194,.3)
}
.author-title{
  padding: 10px
}
.header-img{
  display: inline-block;
  vertical-align: top
}
.author-info{
  display: inline-block;
  margin-left: 5px;
  width: 60%;
  height :40px;
  line-height: 20px;
}
.author-info span{
  display: block;
  cursor :pointer;
  overflow: hidden;
  white-space: nowrap;
  text-overflow :ellipsis;
}
.author-name{
  color: #000;
  font-size: 18px;
  font-weight: bold;
}
.author-time{
  font-size: 14px
}
.icon-btn{
width: 30%;
padding :0 !important;
float: right;

}
.icon-btn.span{
  cursor: pointer
}
.icon-btn.iconfont{
  margin: 0 5px
}
.talk-box{
  margin: 0 50px
}
.talk-box.reply{
  font-size: 16px;
  color: #000
}
.reply-box{
  margin: 10px 0 0 50px;
  background-color: #f4f5f4
}

</style>
