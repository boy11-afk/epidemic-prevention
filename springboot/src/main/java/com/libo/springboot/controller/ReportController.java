package com.libo.springboot.controller;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libo.springboot.common.Result;
import com.libo.springboot.entity.Report;
import com.libo.springboot.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Report report){
        //新增或者更新
        return reportService.saveReport(report);
    }

    //查询数据库所有数据
    @GetMapping
    public List<Report> findAll(){

        return reportService.list();
    }

    @GetMapping("/find/{stuId}")
    public List<Report> find(@PathVariable String stuId){
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stuId",stuId);
        return reportService.list(queryWrapper);
    }


    //根据学号查询单条数据
    @GetMapping("/stuId/{stuId}")
    public Result findOne(@PathVariable String stuId){
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stuId",stuId);
        return Result.success(reportService.getOne(queryWrapper));
    }

    //删除单条学生填报记录
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){

        return reportService.removeById(id);
    }

    //批量删除学生填报记录
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){

        return reportService.removeBatchByIds(ids);
    }

    //分页查询 - mybatis plus的方式
    @GetMapping("/page")
    public IPage<Report> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String college,
                                @RequestParam(defaultValue = "") String stuClass,
                                @RequestParam(defaultValue = "") String username){
        IPage<Report> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        if(!"".equals(college)){
            queryWrapper.like("college",college);
        }
        if(!"".equals(stuClass)){
            queryWrapper.like("stuClass",stuClass);
        }
        if(!"".equals(username)){
            queryWrapper.like("username",username);
        }

        queryWrapper.orderByDesc("id");
        return reportService.page(page,queryWrapper);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Report> list = reportService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","学生id");
        writer.addHeaderAlias("campus","所属校区");
        writer.addHeaderAlias("college","所属学院");
        writer.addHeaderAlias("major","所属专业");
        writer.addHeaderAlias("stuClass","所属班级");
        writer.addHeaderAlias("stuId","学生学号");
        writer.addHeaderAlias("username","学生姓名");
        writer.addHeaderAlias("temperature","当日体温");
        writer.addHeaderAlias("health","健康状况");
        writer.addHeaderAlias("address","当前地址");
        writer.addHeaderAlias("createTime","创建时间");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("学生每日填报信息", "UTF-8");
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
        List<Report> list = reader.readAll(Report.class);

        reportService.saveBatch(list);
        return true;
    }
}
