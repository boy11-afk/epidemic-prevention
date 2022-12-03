<template>
  <el-card style="width: 500px">
    <el-form label-width="100px" size="small">
      <el-upload
          class="avatar-uploader"
          action="http://localhost:9090/file/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
      >
        <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>

      <el-form-item label="所属学院">
        <el-input v-model="form.college" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="教师工号">
        <el-input v-model="form.jobNumber" disabled autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="当前密码">
        <el-input v-model="form.password" type="password" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="教师姓名">
        <el-input v-model="form.adminName" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="教师电话">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="教师邮箱">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="办公地址">
        <el-input type="textarea" v-model="form.address" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
export default {
  name: "Person",
  data() {
    return {
      form: {},
      admin: localStorage.getItem("admin") ? JSON.parse(localStorage.getItem("admin")) : {}
    }
  },
  created() {
    this.getAdmin().then(res => {
      console.log(res)
      this.form = res
    })
  },
  methods: {
    async getAdmin() {
      return (await this.request.get("/admin/jobNumber/" + this.admin.jobNumber)).data
    },
    save() {
      this.request.post("/admin", this.form).then(res => {
        if (res) {
          this.$message.success("完善管理员信息成功！")

          //触发父级更新Admin的方法
          this.$emit("refreshAdmin")

          //更新浏览器存储的用户信息
          this.getAdmin().then( res => {
            res.token = JSON.parse(localStorage.getItem("admin")).token
            localStorage.setItem('admin',JSON.stringify(res))
          })

        } else {
          this.$message.error("完善管理员信息失败！")
        }
      })
    },
    handleAvatarSuccess(res) {
      this.form.avatarUrl = res
    }
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>