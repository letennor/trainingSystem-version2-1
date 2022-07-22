<template>
  <section class="services" id="services">
    <div class="row">
      <div class="col-lg-12">
        <div class="section-heading">
          <h2><em>为您提供</em></h2>
        </div>
      </div>
      <div class="col-lg-12">
        <div class="item-list">
          <div class="item">
            <div class="service-item">
              <!--                <div class="icon">-->
              <img src="../assets/images/study.png" alt="">
              <!--                </div>-->
              <h4>从学到练，一站搞定</h4>
              <p>先学再练，学完巩固，掌握更好</p>
            </div>
          </div>
          <div class="item">
            <div class="service-item">
              <!--                <div class="icon">-->
              <img src="../assets/images/data2.png" alt="">
              <!--                </div>-->
              <h4>个性化训练</h4>
              <p>为你量身匹配训练模式计划，从简到难、先学后练、专项弱点逐个击破</p>
            </div>
          </div>
          <div class="item">
            <div class="service-item">
              <!--                <div class="icon">-->
              <img src="../assets/images/data1.png" alt="">
              <!--                </div>-->
              <h4>实时数据分析</h4>
              <p>针对用户的学习训练情况，系统进行实时数据分析，时刻查看直接的训练数据，记录点滴进步</p>
            </div>
          </div>
          <div class="item">
            <div class="service-item">
              <!--                <div class="icon">-->
              <img src="../assets/images/data4.png" alt="">
              <!--                </div>-->
              <h4>在线讨论与解答</h4>
              <p>在该平台可与各路大神共同探讨学习，讨论交流，也有专业教师在线解答疑惑</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: "middle"
}
</script>

<style scoped>
@import "../assets/css/templatemo-eduwell-style.css";
@import "../assets/css/bootstrap.min.css";
.item-list{
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin: 13px 0px;
  width: 100%;
  flex-flow: row wrap;
}
</style>
