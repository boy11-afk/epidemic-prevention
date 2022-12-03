package com.libo.springboot.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libo.springboot.common.Result;
import com.libo.springboot.entity.Tenter;
import com.libo.springboot.entity.Tout;
import com.libo.springboot.service.ToutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/tout")
public class ToutController {
    @Autowired
    private ToutService toutService;

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Tout tout){
        //新增或者更新
        return toutService.saveTout(tout);
    }

    //查询数据库所有数据
    @GetMapping
    public List<Tout> findAll(){

        return toutService.list();
    }

    @GetMapping("/find/{jobNumber}")
    public List<Tout> find(@PathVariable String jobNumber){
        QueryWrapper<Tout> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("jobNumber",jobNumber);
        return toutService.list(queryWrapper);
    }

    //根据学号查询单条数据
    @GetMapping("/jobNumber/{jobNumber}")
    public Result findOne(@PathVariable String jobNumber){
        QueryWrapper<Tout> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("jobNumber",jobNumber);
        return Result.success(toutService.getOne(queryWrapper));
    }

    //删除单条学生出校记录
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){

        return toutService.removeById(id);
    }

    //批量删除学生出校记录
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){

        return toutService.removeBatchByIds(ids);
    }

    //分页查询 - mybatis plus的方式
    @GetMapping("/page")
    public IPage<Tout> findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String college,
                               @RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "") String place){
        IPage<Tout> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Tout> queryWrapper = new QueryWrapper<>();
        if(!"".equals(college)){
            queryWrapper.like("college",college);
        }
        if(!"".equals(name)){
            queryWrapper.like("name",name);
        }
        if(!"".equals(place)){
            queryWrapper.like("place",place);
        }

        queryWrapper.orderByDesc("id");
        return toutService.page(page,queryWrapper);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Tout> list = toutService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","教师出校记录id");
        writer.addHeaderAlias("campus","工作校区");
        writer.addHeaderAlias("college","所属学院");
        writer.addHeaderAlias("jobNumber","教师工号");
        writer.addHeaderAlias("name","教师姓名");
        writer.addHeaderAlias("phone","教师电话");
        writer.addHeaderAlias("gate","出校门口");
        writer.addHeaderAlias("place","外出地点");
        writer.addHeaderAlias("reason","外出事由");
        writer.addHeaderAlias("specialCase","特殊情况");
        writer.addHeaderAlias("createTime","创建时间");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("教师出校登记信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream tout = response.getOutputStream();
        writer.flush(tout, true);
        tout.close();
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
        List<Tout> list = reader.readAll(Tout.class);

        toutService.saveBatch(list);
        return true;
    }
}
