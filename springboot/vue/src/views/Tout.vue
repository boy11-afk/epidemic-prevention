<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入学院名称" suffix-icon="el-icon-office-building" v-model="college"></el-input>
      <el-input style="width: 200px" placeholder="请输入教师姓名" suffix-icon="el-icon-user" class="ml-5" v-model="name"></el-input>
      <el-input style="width: 200px" placeholder="请输入外出地点" suffix-icon="el-icon-location-information" class="ml-5" v-model="place"></el-input>
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
      <el-upload action="http://localhost:9090/tout/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary"  @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe header-cell-class-name="headerBg"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column prop="id" label="ID" width="40"></el-table-column>
      <el-table-column prop="campus" label="工作校区" width="85"></el-table-column>
      <el-table-column prop="college" label="所属学院" width="180"></el-table-column>
      <el-table-column prop="jobNumber" label="教师工号" width="100"></el-table-column>
      <el-table-column prop="name" label="教师姓名" width="80"></el-table-column>
      <el-table-column prop="phone" label="教师电话" width="120"></el-table-column>
      <el-table-column prop="gate" label="出校门口" width="70"></el-table-column>
      <el-table-column prop="place" label="外出地点" width="90"></el-table-column>
      <el-table-column prop="reason" label="外出事由" width="100"></el-table-column>
      <el-table-column prop="specialCase" label="特殊情况" width="120"></el-table-column>
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

    <el-dialog title="教师出校信息登记" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="small">
        <el-form-item label="工作校区">
          <el-input v-model="form.campus" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="所属学院">
          <el-input v-model="form.college" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="教师工号">
          <el-input v-model="form.jobNumber" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="教师姓名">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="教师电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="出校门口">
          <el-input v-model="form.gate" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="外出地点">
          <el-input v-model="form.place" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="外出事由">
          <el-input v-model="form.reason" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="特殊情况">
          <el-input v-model="form.specialCase" autocomplete="off"></el-input>
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
  name: "Tout",
  data(){
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      college: "",
      name: "",
      place: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      headerBg:'headerBg',
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      this.request.get("http://localhost:9090/tout/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          college:this.college,
          name:this.name,
          place:this.place,
        }
      }).then(res => {
        console.log(res)
        this.tableData=res.records
        this.total=res.total
      })
    },
    save(){
      this.request.post("/tout",this.form).then(res => {
        if(res){
          this.$message.success("保存出校登记信息成功！")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error("保存出校登记信息失败！")
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
      this.request.post("/tout", row).then(res => {
        if (res) {
          this.$message.success("操作成功")
        }
      })
    },
    del(id){
      this.request.delete("/tout/" + id).then(res => {
        if(res){
          this.$message.success("删除出校登记信息成功！")
          this.load()
        }else{
          this.$message.error("删除出校登记信息失败！")
        }
      })
    },
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection = val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id)//因为使用了map集合，将原有的对象数组改变为id数组
      this.request.post("/tout/del/batch",ids).then(res => {
        if(res){
          this.$message.success("批量删除出校登记信息成功！")
          this.load()
        }else{
          this.$message.error("批量删除出校登记信息失败！")
        }
      })
    },
    reset(){
      this.college = ""
      this.name = ""
      this.place = ""
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
      window.open("http://localhost:9090/tout/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>

<style>
.headerBg{
  background-color: #eee !important;
}
</style>