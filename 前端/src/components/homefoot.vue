<template>
  <div class="jumbotron text-center"
       style="margin-bottom:0">
    意见反馈 联系作者<br>
    E-mail: h1229523967@163.com<br>
  </div>
</template>


<script>
export default {
  name: 'homefoot',
  data() {
    return {
      number: 0,
    }
  },
  methods: {
    change(index) {
      this.number = index
    },
  },
}
</script>

<style scoped>
@import '../assets/css/templatemo-eduwell-style.css';
@import '../assets/css/bootstrap.min.css';
.item-list {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin: 13px 0px;
  width: 100%;
  flex-flow: row wrap;
}
</style>
