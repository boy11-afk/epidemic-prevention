<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入学院名称" suffix-icon="el-icon-office-building" v-model="college"></el-input>
      <el-input style="width: 200px" placeholder="请输入学生班级" suffix-icon="el-icon-search" class="ml-5" v-model="stuClass"></el-input>
      <el-input style="width: 200px" placeholder="请输入学生姓名" suffix-icon="el-icon-user" class="ml-5" v-model="username"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-upload action="http://localhost:9090/enter/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary"  @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe header-cell-class-name="headerBg"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column prop="id" label="ID" width="40"></el-table-column>
      <el-table-column prop="campus" label="所属校区" width="85"></el-table-column>
      <el-table-column prop="college" label="所属学院" width="160"></el-table-column>
      <el-table-column prop="major" label="所属专业" width="100"></el-table-column>
      <el-table-column prop="stuClass" label="所属班级" width="80"></el-table-column>
      <el-table-column prop="stuId" label="学生学号" width="70"></el-table-column>
      <el-table-column prop="username" label="学生姓名" width="70"></el-table-column>
      <el-table-column prop="phone" label="学生电话" width="80"></el-table-column>
      <el-table-column prop="location" label="出校地点" width="100"></el-table-column>
      <el-table-column label="健康码">
        <img src="http://localhost:9090/file/660e3a24ced24791b63e8528a96fad8d.jpg" alt="" style="width: 90px;height: 90px">
      </el-table-column>
      <el-table-column label="行程码">
        <img src="http://localhost:9090/file/73d45e596b7844509ee92ff23164a55b.jpg" alt="" style="width: 90px;height: 90px">
      </el-table-column>
      <el-table-column label="是否通过" >
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="学生入校信息记录" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="small">
        <el-form-item label="所属校区">
          <el-input v-model="form.campus" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="所属学院">
          <el-input v-model="form.college" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="所属专业">
          <el-input v-model="form.major" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="所属班级">
          <el-input v-model="form.stuClass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学生学号">
          <el-input v-model="form.stuId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学生姓名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学生电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="出校地点">
          <el-input v-model="form.location" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="健康码">
          <el-upload
              class="avatar-uploader"
              action="http://localhost:9090/file/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess1"
          >
            <img v-if="form.healthCode" :src="form.healthCode" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="行程码">
          <el-upload
              class="avatar-uploader"
              action="http://localhost:9090/file/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess2"
          >
            <img v-if="form.journeyCode" :src="form.journeyCode" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Enter",
  data(){
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      college: "",
      stuClass: "",
      username: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      headerBg:'headerBg',
      healthCode: "",
      journeyCode: "",
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      this.request.get("http://localhost:9090/enter/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          college:this.college,
          stuClass:this.stuClass,
          username:this.username,
        }
      }).then(res => {
        console.log(res)
        this.tableData=res.records
        this.total=res.total
      })
    },
    save(){
      this.request.post("/enter",this.form).then(res => {
        if(res){
          this.$message.success("保存入校申请信息成功！")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error("保存入校申请信息失败！")
        }
      })
    },
    handAdd(){
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row){
      this.form = row
      this.dialogFormVisible = true
    },
    changeEnable(row) {
      this.request.post("/enter", row).then(res => {
        if (res) {
          this.$message.success("操作成功")
        }
      })
    },
    del(id){
      this.request.delete("/enter/" + id).then(res => {
        if(res){
          this.$message.success("删除入校申请信息成功！")
          this.load()
        }else{
          this.$message.error("删除入校申请信息失败！")
        }
      })
    },
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection = val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id)//因为使用了map集合，将原有的对象数组改变为id数组
      this.request.post("/enter/del/batch",ids).then(res => {
        if(res){
          this.$message.success("批量删除入校申请信息成功！")
          this.load()
        }else{
          this.$message.error("批量删除入校申请信息失败！")
        }
      })
    },
    reset(){
      this.college = ""
      this.stuClass = ""
      this.username = ""
      this.load()
    },
    handleSizeChange(pageSize){
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    exp() {
      window.open("http://localhost:9090/enter/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    handleAvatarSuccess1(res) {
      this.form.healthCode = res
      this.load()
      this.$message.success('success')
    },
    handleAvatarSuccess2(res) {
      this.form.journeyCode = res
      this.load()
      this.$message.success('success')
    },
  }
}
</script>

<style>
.headerBg{
  background-color: #eee !important;
}
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