<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100vh;overflow-x: hidden"
           background-color="rgb(48,65,86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           class="el-menu-vertical-demo"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px;line-height: 60px;text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px;position: relative;top: 5px;margin-right: 5px">
      <b style="color: white" v-show="logoTextShow">疫情防控系统</b>
    </div>
    <el-menu-item index="/home">
      <template slot="title">
        <i class="el-icon-house"></i>
        <span slot="title">系统主页</span>
      </template>
    </el-menu-item>
    <el-submenu index="0">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">系统管理</span>
      </template>
      <el-menu-item index="/admin">
          <i class="el-icon-user-solid"></i>
          <span slot="title">管理员管理</span>
      </el-menu-item>
      <el-menu-item index="/teacher">
        <i class="el-icon-s-custom"></i>
        <span slot="title">教师管理</span>
      </el-menu-item>
      <el-menu-item index="/student">
        <i class="el-icon-user"></i>
        <span slot="title">学生管理</span>
      </el-menu-item>
      <el-menu-item index="/file">
        <i class="el-icon-document"></i>
        <span slot="title">文件管理</span>
      </el-menu-item>
    </el-submenu>
    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-s-management"></i>
        <span slot="title">防控管理（学生）</span>
      </template>
      <el-menu-item index="/report">
        <i class="el-icon-s-order"></i>
        <span slot="title">每日填报</span>
      </el-menu-item>
      <el-menu-item index="/out">
        <i class="el-icon-s-marketing"></i>
        <span slot="title">出校申请</span>
      </el-menu-item>
      <el-menu-item index="/enter">
        <i class="el-icon-school"></i>
        <span slot="title">入校申请</span>
      </el-menu-item>
      <el-menu-item index="/article">
        <i class="el-icon-document"></i>
        <span slot="title">防疫公告</span>
      </el-menu-item>
      <el-menu-item index="/acid">
        <i class="el-icon-set-up"></i>
        <span slot="title">核酸预约</span>
      </el-menu-item>
    </el-submenu>
    <el-submenu index="4">
      <template slot="title">
        <i class="el-icon-s-management"></i>
        <span slot="title">防控管理（教师）</span>
      </template>
      <el-menu-item index="/treport">
        <i class="el-icon-s-order"></i>
        <span slot="title">每日填报</span>
      </el-menu-item>
      <el-menu-item index="/tout">
        <i class="el-icon-s-marketing"></i>
        <span slot="title">出校登记</span>
      </el-menu-item>
      <el-menu-item index="/tenter">
        <i class="el-icon-school"></i>
        <span slot="title">入校登记</span>
      </el-menu-item>
      <el-menu-item index="/tarticle">
        <i class="el-icon-document"></i>
        <span slot="title">防疫公告</span>
      </el-menu-item>
      <el-menu-item index="/tacid">
        <i class="el-icon-set-up"></i>
        <span slot="title">核酸预约</span>
      </el-menu-item>
    </el-submenu>
    <el-submenu index="6">
      <template slot="title">
        <i class="el-icon-s-management"></i>
        <span slot="title">防控管理（访客）</span>
      </template>
      <el-menu-item index="/visitorEnter">
        <i class="el-icon-school"></i>
        <span slot="title">入校申请</span>
      </el-menu-item>
      <el-menu-item index="/visitorOut">
        <i class="el-icon-s-marketing"></i>
        <span slot="title">出校申请</span>
      </el-menu-item>
    </el-submenu>
    <el-submenu index="8">
      <template slot="title">
        <i class="el-icon-data-analysis"></i>
        <span slot="title">疫情动态</span>
      </template>
<!--      <el-menu-item index="">-->
<!--        <i class="el-icon-pie-chart"></i>-->
<!--        <span slot="title">数据动态</span>-->
<!--      </el-menu-item>-->
      <el-menu-item index="/regional">
        <i class="el-icon-location"></i>
        <span slot="title">地区疫情</span>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  }
}

</script>

<style scoped>
.el-menu--collapse span {
  visibility: hidden;
}
</style>