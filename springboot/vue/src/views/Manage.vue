<template>
  <el-container style="min-height: 100vh">

    <el-aside :width="sideWidth + 'px'" style="background-color: rgb(238, 241, 246); height: 100%; box-shadow: 2px 0 6px rgb(0 21 41);">
      <Aside :collapse="isCollapse" :logoTextShow="logoTextShow"/>
    </el-aside>

    <el-container>
      <el-header style="border-bottom: 1px solid #ccc;">
        <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :admin="admin"/>
      </el-header>

      <el-main>
        <!--表示当前页面的子路由会在<router-view />里面展示-->
        <router-view @refreshAdmin = "getAdmin"/>
      </el-main>

    </el-container>
  </el-container>
</template>

<script>

import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: 'Manage',
  data(){
    return{
      msg:"hello 李博小可爱",
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse:false,
      sideWidth:200,
      logoTextShow:true,
      admin: localStorage.getItem("admin") ? JSON.parse(localStorage.getItem("admin")) : {}
    }
  },
  components:{
    Aside,
    Header
  },
  methods:{
    collapse(){//点击收缩按钮
      this.isCollapse=!this.isCollapse;
      if (this.isCollapse){ //收缩
        this.sideWidth = 64;
        this.collapseBtnClass = 'el-icon-s-unfold';
        this.logoTextShow = false;
      }else{//展开
        this.sideWidth = 200;
        this.collapseBtnClass = 'el-icon-s-fold';
        this.logoTextShow = true;
      }
    },
    getAdmin(){
      //从后台获取数据
      this.request.get("/admin/jobNumber/" + this.admin.jobNumber).then(res => {
        this.admin = res.data
      })
    }
  }
}
</script>


