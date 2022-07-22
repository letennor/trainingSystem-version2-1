<template>
<div>
  <el-row  :gutter="20">
    <el-col :span="17">
      <el-card v-show="active===1" shadow="never">
        <div slot="header" class="clearfix">
      <span style="font-size: 25px">
        <i style="color:#49d53e;" class="el-icon-message-solid"></i>系统消息</span> <el-tag type="danger" size="mini" v-if="unreadNotifyCount != 0">{{
            `${unreadNotifyCount}条未读`
          }}</el-tag>
        </div>
        <div
            class="item"
            v-for="(value, index) in notifys"
            :key="index"
        >

          <el-card style="margin-top: 5px;" shadow="hover">
              <div @mouseover="showSee = index" @mouseleave="showSee = -1">
                <el-badge is-dot class="item" v-if="!value.isCheck"
                          style="display: inline-block; vertical-align: top">
                  <el-tag type="primary" effect="dark">通知</el-tag>
                </el-badge>
                <el-tag v-else type="primary" effect="dark" style="display: inline-block; vertical-align: top">通知</el-tag>

                <span style="display: inline-block;"></span> {{ value.notificationInfo | ellipsis }}
                <el-link
                    v-show="showSee === index"
                    style="margin-left: 20px"
                    @click="readNotify(value)"
                >查看<i class="el-icon-view el-icon--right"></i>
                </el-link>
                <span style="float: right">{{
                    readerTime(value.gmtCreate)
                  }}</span>
              </div>

          </el-card>
        </div>
      </el-card>
      <el-card v-show="active===2" shadow="never">
        <div slot="header" class="clearfix">
      <span style="font-size: 25px">
       <i style="color:#3e8fd5;" class="el-icon-s-comment"></i>我的申请</span>
        </div>
        <el-card
            shadow="hover"
            style="margin-top: 10px"
            v-for="(item, index) in sendNotifys"
            :key="index"
        >
          <el-descriptions border :column="1">
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-tickets"></i>
                内容
              </template>
              {{ item.notificationInfo }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                <i class="el-icon-date"></i>
                时间
              </template>
              {{ readerTime(item.gmtCreate) }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card shadow="never">
        <div class="block">
        <el-timeline>
          <el-timeline-item size="large"  placement="top" :color="active===1 ? '#3e8fd5':''">
            <h3 @click="active=1" style="cursor: pointer;">
              <i style="color:#49d53e;" class="el-icon-message-solid"></i>系统消息
            </h3>

          </el-timeline-item>
          <el-timeline-item size="large"  placement="top"  :color="active===2 ? '#3e8fd5':''">
            <h3 @click="active=2" style="cursor: pointer;"> <i style="color:#3e8fd5;" class="el-icon-s-comment"></i>我的申请</h3>
          </el-timeline-item>
        </el-timeline>
        </div>
      </el-card>
    </el-col>
  </el-row>
  <el-dialog
      title="通知内容"
      :visible.sync="dialogRead"
      :append-to-body="true"
  >
    {{ content }}
  </el-dialog>

</div>
</template>

<script>
import request from "@/api/request";

export default {
  name: "notice",
  data(){
    return{
      notifys: [], //收到的通知
      sendNotifys: [], //发送的通知
      unreadNotifyCount: 0, // 未读通知数量\
      active:1,
      showSee: -1,
      dialogRead: false,
    }
  },
  filters: {
    ellipsis(value) {
      if (!value) return "";
      if (value.length > 15) {
        return value.slice(0, 15) + "......";
      }
      return value;
    },
  },
  methods:{
    getNotify() {
      request({
        url: "/notification/received",
        method: "POST",
        data: {
          userId: sessionStorage.getItem("token"),
        },
      }).then((res) => {
        this.unreadNotifyCount = 0;
        // console.log(res.data);
        this.notifys = res.data;
        // console.log(this.notifys);
        this.notifys.forEach((notify) => {
          // console.log(notify);
          if (notify.isCheck == 0) {
            this.unreadNotifyCount++;
          }
          // console.log(this.unreadNotifyCount);
        });
        this.setdate(this.notifys);
      });
    },
    getSendNotify() {
      request({
        url: "/notification/send",
        method: "POST",
        data: {
          userId: sessionStorage.getItem("token"),
        },
      }).then((res) => {
        this.sendNotifys = res.data;
      });
    },
    readerTime(data) {
      var dateee = new Date(data).toJSON();
      return new Date(+new Date(dateee) + 8 * 3600 * 1000)
          .toISOString()
          .replace(/T/g, "")
          .replace(/\.[\d]{3}Z/, "");
    },
    setdate(data) {
      //date sort
      data.sort(function (a, b) {
        return a.gmtCreate < b.gmtCreate ? 1 : -1;
      });
    },
    readNotify(value) {
      request({
        url: "/confirmNotification",
        method: "post",
        data: {
          notificationId: value.notificationId,
        },
      }).then((res) => {
        if (res.data === 1) {
          this.content = value.notificationInfo;
          this.dialogRead = true;
          this.getNotify();
          this.$bus.$emit("countSub");
        }
      });
    },
  },
  created() {
    this.getNotify();
    this.getSendNotify();
  }

}
</script>

<style scoped>

</style>