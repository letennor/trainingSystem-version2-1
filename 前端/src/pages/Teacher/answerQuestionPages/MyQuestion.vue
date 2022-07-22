<template>
  <div>
    <el-empty description="暂无未处理的提问"
              v-if="messageList.length == 0"></el-empty>
    <el-row v-if="messageList.length != 0">
      <template>
        <el-carousel :interval="1000"
                     arrow="hover"
                     :autoplay="false"
                     height="350px"
                     indicator-position="none"
                     style="background-color: white"
                     @change="changeMessage">
          <el-carousel-item v-for="message in messageList"
                            :key="message.messageId"
                            style="margin-left: 10px; margin-right: 10px">
            <div style="
                width: 90%;
                padding: 20px;
                margin-top: 40px;
                margin-left: 40px;
              ">
              <el-descriptions class="margin-top"
                               :title="message.messageTitle"
                               :column="2"
                               style="">
                <template slot="extra">
                  <el-button type="danger"
                             size="small"
                             @click="deleteMessage(message)">删除</el-button>
                </template>
                <template slot="extra">
                  <el-button type="primary"
                             size="small"
                             @click="replyMessage(message)">回复</el-button>
                </template>
                <el-descriptions-item label="内容">{{
                  message.messageContent
                }}</el-descriptions-item>
              </el-descriptions>
              <div style="margin-top: 30px">
                <p style="font-size: 14px">
                  <el-tag style="margin-right: 3px"
                          size="small">提问者</el-tag>{{ message.name }}
                </p>
                <p style="font-size: 14px">
                  <el-tag style="margin-right: 3px"
                          size="small">提问时间</el-tag>{{ renderTime(message.gmtCreate) }}
                </p>
              </div>
            </div>
            <!-- <el-col :span="24" style="font-size: 25px">
              <strong>{{ message.messageTitle }}</strong>
            </el-col>

            <el-col :span="24"> <br />{{ message.messageContent }} </el-col> -->

            <!-- <el-col :span="12" style="margin-top: 10px">
              <el-tag style="margin-right: 3px">提问者</el-tag
              >{{ message.name }}
            </el-col>
            <el-col :span="12" style="margin-top: 10px">
              <el-tag style="margin-right: 3px">提问时间</el-tag
              >{{ renderTime(message.gmtCreate) }}
            </el-col> -->
            <!-- <el-col :span="24" style="position: absolute; top: 340px">
              <el-button
                type="danger"
                plain
                style="width: 200px"
                @click="deleteMessage(message)"
                >删除</el-button
              >

              <el-button
                type="success"
                plain
                style="width: 200px; margin-left: 755px"
                @click="replyMessage(message)"
                >回复</el-button
              >
            </el-col> -->
          </el-carousel-item>
        </el-carousel>
      </template>

      <el-col :span="24"
              style="height: 290px; margin-top: 10px"
              v-if="toReply">
        <el-form :model="reply"
                 ref="replyForm"
                 :rules="rules">
          <el-form-item prop="replyInfo">
            <el-input type="textarea"
                      placeholder="请输入回复内容"
                      v-model="reply.replyInfo"
                      :autosize="{ minRows: 3, maxRows: 8 }">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary"
                       @click="sendReply">提交</el-button>
          </el-form-item>
        </el-form>
        <!-- <el-input
          type="textarea"
          :rows="10"
          placeholder="请输入内容"
          ref="reply"
          style="font-size: 15px"
          v-model="messageReply"
        >
        </el-input>

        <el-button
          type="success"
          plain
          style="margin-top: 10px; margin-left: 955px; width: 200px"
          @click="sendReply"
          >发送</el-button
        > -->
      </el-col>
    </el-row>
  </div>
</template>
<script>
import request from '../../../api/request'

export default {
  name: 'MyQuestion',
  data() {
    return {
      messageList: [],
      messageReply: null,
      chooseReplyMessage: null,
      messageTime: null,
      toReply: false,
      reply: {
        replyInfo: '',
      },
      rules: {
        replyInfo: [
          { required: true, message: '请输入回复内容', trigger: 'blur' },
        ],
      },
    }
  },
  methods: {
    getMessageList() {
      //请求所有评论
      request({
        url: '/getTeacherMessage',
        method: 'post',
        data: {
          teacherId: sessionStorage.getItem('token'),
          isReplied: 0,
        },
      }).then((res) => {
        console.log(res)
        this.messageList = res.data
      })
    },

    deleteMessage(message) {
      console.log('删除', message)
      this.$confirm('确定要删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          request({
            url: 'deleteMessage',
            method: 'post',
            data: {
              messageId: message.messageId,
            },
          }).then((res) => {
            console.log(res)
            this.getMessageList()
            //刷新小红点
            this.$bus.$emit('refreshMenu', 'refreshMenu')

            this.$message({
              type: 'success',
              message: '删除成功！',
            })
          })
        })
        .catch(() => {
          console.log('取消删除')
        })
    },

    replyMessage(message) {
      this.chooseReplyMessage = message
      console.log('回复', this.chooseReplyMessage)
      this.toReply = true
      //焦点移到下方输入框
      // this.$refs.reply.focus();
    },
    changeMessage() {
      this.toReply = false
    },
    renderTime(date) {
      var dateee = new Date(date).toJSON()
      return new Date(+new Date(dateee) + 8 * 3600 * 1000)
        .toISOString()
        .replace(/T/g, ' ')
        .replace(/\.[\d]{3}Z/, '')
    },

    sendReply() {
      this.$refs.replyForm.validate((valid) => {
        if (valid) {
          request({
            url: '/replyMessage',
            method: 'post',
            data: {
              messageId: this.chooseReplyMessage.messageId,
              messageReply: this.reply.replyInfo,
            },
          }).then((res) => {
            this.messageReply = ''
            console.log(res),
              this.getMessageList(),
              //刷新menu小红点
              this.$bus.$emit('refreshMenu', 'refreshMenu')
            this.$message({
              type: 'success',
              message: '回复成功！',
            })
          })
        } else {
          console.log('error submit')
        }
      })
    },
  },

  mounted() {
    this.getMessageList()
  },
}
</script>

<style scoped>
.el-row {
  width: 1200px;
  position: relative;
  margin: auto;
  /* color: gainsboro; */
}

/* * {
  border: 1px red solid;
} */
</style>
