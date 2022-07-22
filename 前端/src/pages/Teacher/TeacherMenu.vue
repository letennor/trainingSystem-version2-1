<template>
  <div>
    <div style="padding: 10px 20px">
      <h5
        style="text-align: center; margin-top: 10px; color: #fff; margin-bottom: 20px"
      >
        教师系统
      </h5>
      <el-divider class="aside-divider"></el-divider>
    </div>
    <el-menu
      default-active="1-4-1"
      class="el-menu-vertical-demo"
      text-color="#cddaf0"
      active-text-color="#fff"
      :collapse="isCollapse"
    >
      <el-menu-item
        @click="clickMenu(item)"
        v-for="item in router"
        :index="item.path"
        :key="item.path"
      >
        <!-- <i class="el-icon-menu"></i>-->

        <i class="el-icon-folder-add" v-if="item.title == '制作试卷'"></i>

        <i class="el-icon-document" v-if="item.title == '工作信息'"></i>

        <i class="el-icon-chat-line-square" v-if="item.title == '学生提问'"></i>

        <i class="el-icon-edit" v-if="item.title == '上传题目'"></i>

        <i class="el-icon-view" v-if="item.title == '学生情况'"></i>

        <i class="el-icon-star-off" v-if="item.title == '资源管理'"></i>
        <span 
          >{{ item.title }}
          <el-badge
            :value="needReplyNumber"
            :max="99"
            class="item"
            v-if="needReplyNumber > 0 && item.title == '学生提问'"
          >
          </el-badge>
        </span>
        <el-divider class="aside-divider"></el-divider>
      </el-menu-item>
    </el-menu>
  </div>
</template>
<script>
import request from "../../api/request";

export default {
  data() {
    return {
      isCollapse: false,
      needReplyNumber: null,
      router: [
        {
          name: "BasicInfo",
          path: "/BasicInfo",
          title: "工作信息",
        },
        {
          name: "CreateTest",
          path: "/CreateTest",
          title: "制作试卷",
        },

        {
          name: "UploadQuestion",
          path: "/UploadQuestion",
          title: "上传题目",
        },
        {
          name: "MyQuestion",
          path: "/MyQuestion",
          title: "学生提问",
        },

        {
          name: "StudentInfo",
          path: "/StudentInfo",
          title: "学生情况",
        },

        {
          name: "SourceManagement",
          path: "/SourceManagement",
          title: "资源管理",
        },
      ],
    };
  },
  methods: {
    clickMenu(item) {
      console.log("点击了", item.name);
      this.$router.push({
        name: item.name,
      });
    },

    getMessageList() {
      //请求所有评论
      request({
        url: "/getTeacherMessage",
        method: "post",
        data: {
          teacherId: sessionStorage.getItem("token"),
          isReplied: 0,
        },
      }).then((res) => {
        this.needReplyNumber = res.data.length;
      });
    },
  },

  mounted() {
    this.getMessageList();
  },
};
</script>

<style scoped>
.route-link {
  text-decoration: none;
}

.el-menu {
  background-color: #4e73df;
  border-right: none;
}
.el-menu-item i {
  color: #cddaf0;
}
.el-menu .is-active {
  font-weight: 700;
}
.el-menu-item:hover {
  background-color: rgb(69, 66, 238);
}
.el-menu-item:focus {
  background-color: rgb(69, 66, 238);
}
.aside-divider {
  margin: 0px 0px;
  background-color: #829be7;
  /* width: 80%;
  padding: 0px 10px; */
}
</style>
