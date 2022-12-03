<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入访客姓名" suffix-icon="el-icon-user" v-model="name"></el-input>
      <el-input style="width: 200px" placeholder="请输入身份证号" suffix-icon="el-icon-aim" class="ml-5" v-model="idNum"></el-input>
      <el-input style="width: 200px" placeholder="请输入到访校区" suffix-icon="el-icon-school" class="ml-5" v-model="campus"></el-input>
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
      <el-upload action="http://localhost:9090/visitorOut/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary"  @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe header-cell-class-name="headerBg"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="id" label="ID" width="50"></el-table-column>
      <el-table-column prop="name" label="访客姓名" width="70"></el-table-column>
      <el-table-column prop="idNum" label="身份证号" width="150"></el-table-column>
      <el-table-column prop="phone" label="手机号码" width="120"></el-table-column>
      <el-table-column prop="campus" label="到访校区" width="80"></el-table-column>
      <el-table-column prop="reason" label="入校缘由" width="80"></el-table-column>
      <el-table-column prop="health" label="健康状况" width="70"></el-table-column>
      <el-table-column prop="temperature" label="访客体温" width="70"></el-table-column>
      <el-table-column label="健康码" width="120px">
        <img src="http://localhost:9090/file/660e3a24ced24791b63e8528a96fad8d.jpg" alt="" style="width: 90px;height: 90px">
      </el-table-column>
      <el-table-column label="行程码" width="120px">
        <img src="http://localhost:9090/file/73d45e596b7844509ee92ff23164a55b.jpg" alt="" style="width: 90px;height: 90px">
      </el-table-column>
      <el-table-column label="是否通过" width="80">
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

    <el-dialog title="访客入校申请" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="small">
        <el-form-item label="访客姓名">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="form.idNum" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="到访校区">
          <el-input v-model="form.campus" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="入校缘由">
          <el-input v-model="form.reason" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="健康状况">
          <el-input v-model="form.health" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="访客体温">
          <el-input v-model="form.temperature" autocomplete="off"></el-input>
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
  name: "VisitorOut",
  data(){
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      idNum: "",
      campus: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      headerBg:'headerBg',
      healthCode: "",
      journeyCode: ""
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      this.request.get("http://localhost:9090/visitorOut/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          name:this.name,
          idNum:this.idNum,
          campus:this.campus,
        }
      }).then(res => {
        console.log(res)
        this.tableData=res.records
        this.total=res.total
      })
    },
    save(){
      this.request.post("/visitorOut",this.form).then(res => {
        if(res){
          this.$message.success("保存出校申请信息成功！")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error("保存出校申请信息失败！")
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
      this.request.post("/visitorOut", row).then(res => {
        if (res) {
          this.$message.success("操作成功")
        }
      })
    },
    del(id){
      this.request.delete("/visitorOut/" + id).then(res => {
        if(res){
          this.$message.success("删除出校申请信息成功！")
          this.load()
        }else{
          this.$message.error("删除出校申请信息失败！")
        }
      })
    },
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection = val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id)//因为使用了map集合，将原有的对象数组改变为id数组
      this.request.post("/visitorOut/del/batch",ids).then(res => {
        if(res){
          this.$message.success("批量删除出校申请信息成功！")
          this.load()
        }else{
          this.$message.error("批量删除出校申请信息失败！")
        }
      })
    },
    reset(){
      this.name = ""
      this.idNum = ""
      this.campus = ""
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
      window.open("http://localhost:9090/visitorOut/export")
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