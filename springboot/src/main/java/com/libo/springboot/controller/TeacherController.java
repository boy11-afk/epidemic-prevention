package com.libo.springboot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libo.springboot.common.Constants;
import com.libo.springboot.common.Result;
import com.libo.springboot.controller.dto.TeacherDTO;
import com.libo.springboot.entity.Teacher;
import com.libo.springboot.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/login")
    public Result login(@RequestBody TeacherDTO teacherDTO) {
        String jobNumber = teacherDTO.getJobNumber();
        String password = teacherDTO.getPassword();
        if (StrUtil.isBlank(jobNumber) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        TeacherDTO dto = teacherService.login(teacherDTO);
        return Result.success(dto);
    }

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Teacher teacher){
        //新增或者更新
        return teacherService.saveTeacher(teacher);
    }

    //查询数据库所有数据
    @GetMapping
    public List<Teacher> findAll(){
        return teacherService.list();
    }

    //通过教师工号查询某个教师的信息
    @GetMapping("/jobNumber/{jobNumber}")
    public Result findOne(@PathVariable String jobNumber){
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("jobNumber",jobNumber);
        return Result.success(teacherService.getOne(queryWrapper));
    }

    //删除单条学生记录
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
        return teacherService.removeById(id);
    }

    //批量删除学生记录
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){

        return teacherService.removeBatchByIds(ids);
    }
    //分页查询 - mybatis plus的方式
    @GetMapping("/page")
    public IPage<Teacher> findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String college,
                                   @RequestParam(defaultValue = "") String name,
                                   @RequestParam(defaultValue = "") String location){
        IPage<Teacher> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        if(!"".equals(college)){
            queryWrapper.like("college",college);
        }
        if(!"".equals(name)){
            queryWrapper.like("name",name);
        }
        if(!"".equals(location)){
            queryWrapper.like("location",location);
        }

        queryWrapper.orderByDesc("id");
        return teacherService.page(page,queryWrapper);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Teacher> list = teacherService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","教师id");
        writer.addHeaderAlias("campus","工作校区");
        writer.addHeaderAlias("college","所属学院");
        writer.addHeaderAlias("jobNumber","教师工号");
        writer.addHeaderAlias("name","教师姓名");
        writer.addHeaderAlias("password","教师密码");
        writer.addHeaderAlias("phone","教师电话");
        writer.addHeaderAlias("email","教师邮箱");
        writer.addHeaderAlias("address","办公地址");
        writer.addHeaderAlias("location","家庭住址");
        writer.addHeaderAlias("createTime","创建时间");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("教师信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //  方式1：(推荐) 通过 javabean的方式读取Excel内的对象，
        //  但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Teacher> list = reader.readAll(Teacher.class);

        teacherService.saveBatch(list);
        return true;
    }
}
