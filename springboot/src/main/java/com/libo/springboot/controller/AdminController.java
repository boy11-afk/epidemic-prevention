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
import com.libo.springboot.controller.dto.AdminDTO;
import com.libo.springboot.entity.Admin;
import com.libo.springboot.service.AdminService;
import com.libo.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    //登录控制方法
    @PostMapping("/login")
    public Result login(@RequestBody AdminDTO adminDTO) {
        String jobNumber = adminDTO.getJobNumber();
        String password = adminDTO.getPassword();
        if (StrUtil.isBlank(jobNumber) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        AdminDTO dto = adminService.login(adminDTO);
        return Result.success(dto);
    }

    //注册控制方法
    @PostMapping("/register")
    public Result register(@RequestBody AdminDTO adminDTO) {
        String jobNumber = adminDTO.getJobNumber();
        String password = adminDTO.getPassword();
        if (StrUtil.isBlank(jobNumber) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误");
        }
        return Result.success(adminService.register(adminDTO));
    }

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody Admin admin){
        //新增或者更新
        return adminService.saveAdmin(admin);
    }

    //查询数据库所有数据
    @GetMapping
    public List<Admin> findAll(){
        return adminService.list();
    }

    @GetMapping("/jobNumber/{jobNumber}")
    public Result findOne(@PathVariable String jobNumber){
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("jobNumber",jobNumber);
        return Result.success(adminService.getOne(queryWrapper));
    }

    //删除单条管理员记录
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
        return adminService.removeById(id);
    }

    //批量删除管理员记录
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return adminService.removeBatchByIds(ids);
    }


    //分页查询接口
    //接口路径为/admin/page
    //@RequestParam接收数据
    //limit第一个参数 = (pageNum - 1) * pageSize
    //pageSize
    /*mybatis的方式进行分页查询
    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam String adminName){
        pageNum = (pageNum - 1) * pageSize;
        List<Admin> data = adminMapper.selectPage(pageNum,pageSize,adminName);
        Integer total = adminMapper.selectTotal(adminName);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }
    */
    //分页查询 - mybatis plus的方式
    @GetMapping("/page")
    public IPage<Admin> findPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue = "") String college,
                                 @RequestParam(defaultValue = "") String jobNumber,
                                 @RequestParam(defaultValue = "") String adminName){
        IPage<Admin> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if(!"".equals(college)){
            queryWrapper.like("college",college);
        }
        if(!"".equals(jobNumber)){
            queryWrapper.like("jobNumber",jobNumber);
        }
        if(!"".equals(adminName)){
            queryWrapper.like("adminName",adminName);
        }

        Admin currentAdmin = TokenUtils.getCurrentAdmin();
        System.out.println("获取当前用户信息++++++++++++++++"+currentAdmin.getAdminName());
        queryWrapper.orderByDesc("id");
        return adminService.page(page,queryWrapper);
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Admin> list = adminService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","管理员id");
        writer.addHeaderAlias("college","所属学院");
        writer.addHeaderAlias("jobNumber","教师工号");
        writer.addHeaderAlias("adminName","教师姓名");
        writer.addHeaderAlias("password","密码");
        writer.addHeaderAlias("phone","教师电话");
        writer.addHeaderAlias("email","教师邮箱");
        writer.addHeaderAlias("address","办公地址");
        writer.addHeaderAlias("avatarUrl","管理员头像");
        writer.addHeaderAlias("createTime","创建时间");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("管理员信息", "UTF-8");
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
        List<Admin> list = reader.readAll(Admin.class);

        adminService.saveBatch(list);
        return true;
    }

}
