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
import com.libo.springboot.controller.dto.VisitorOutDTO;
import com.libo.springboot.entity.VisitorOut;
import com.libo.springboot.service.VisitorOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/visitorOut")
public class VisitorOutController {
    @Autowired
    private VisitorOutService visitorOutService;

    @PostMapping("/login")
    public Result login(@RequestBody VisitorOutDTO visitorOutDTO) {
        String name = visitorOutDTO.getName();
        String idNum = visitorOutDTO.getIdNum();
        String phone = visitorOutDTO.getPhone();
        if (StrUtil.isBlank(name) || StrUtil.isBlank(idNum) || StrUtil.isBlank(phone)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        VisitorOutDTO dto = visitorOutService.login(visitorOutDTO);
        return Result.success(dto);
    }

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody VisitorOut visitorOut){
        //新增或者更新
        return visitorOutService.saveVisitorOut(visitorOut);
    }

    //查询数据库所有数据
    @GetMapping
    public List<VisitorOut> findAll(){

        return visitorOutService.list();
    }

    @GetMapping("/find/{idNum}")
    public List<VisitorOut> find(@PathVariable String idNum){
        QueryWrapper<VisitorOut> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("idNum",idNum);
        return visitorOutService.list(queryWrapper);
    }

    //根据访客身份证号查询单条数据
    @GetMapping("/idNum/{idNum}")
    public Result findOne(@PathVariable String idNum){
        QueryWrapper<VisitorOut> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("idNum",idNum);
        return Result.success(visitorOutService.getOne(queryWrapper));
    }

    //删除单条学生出校记录
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){

        return visitorOutService.removeById(id);
    }

    //批量删除学生出校记录
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){

        return visitorOutService.removeBatchByIds(ids);
    }

    //分页查询 - mybatis plus的方式
    @GetMapping("/page")
    public IPage<VisitorOut> findPage(@RequestParam Integer pageNum,
                                      @RequestParam Integer pageSize,
                                      @RequestParam(defaultValue = "") String idNum,
                                      @RequestParam(defaultValue = "") String name,
                                      @RequestParam(defaultValue = "") String place){
        IPage<VisitorOut> page = new Page<>(pageNum,pageSize);
        QueryWrapper<VisitorOut> queryWrapper = new QueryWrapper<>();
        if(!"".equals(idNum)){
            queryWrapper.like("idNum",idNum);
        }
        if(!"".equals(name)){
            queryWrapper.like("name",name);
        }
        if(!"".equals(place)){
            queryWrapper.like("place",place);
        }

        queryWrapper.orderByDesc("id");
        return visitorOutService.page(page,queryWrapper);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<VisitorOut> list = visitorOutService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","访客出校id");
        writer.addHeaderAlias("name","访客姓名");
        writer.addHeaderAlias("idNum","身份证号");
        writer.addHeaderAlias("phone","访客电话");
        writer.addHeaderAlias("campus","到访校区");
        writer.addHeaderAlias("reason","入校缘由");
        writer.addHeaderAlias("healthCode","健康码");
        writer.addHeaderAlias("journeyCode","行程码");
        writer.addHeaderAlias("health","健康状况");
        writer.addHeaderAlias("temperature","当前体温");
        writer.addHeaderAlias("enable","是否通过");
        writer.addHeaderAlias("createTime","创建时间");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("访客出校申请信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream visitorOut = response.getOutputStream();
        writer.flush(visitorOut, true);
        visitorOut.close();
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
        List<VisitorOut> list = reader.readAll(VisitorOut.class);

        visitorOutService.saveBatch(list);
        return true;
    }
}
