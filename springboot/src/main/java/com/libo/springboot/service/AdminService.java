package com.libo.springboot.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.common.Constants;
import com.libo.springboot.controller.dto.AdminDTO;
import com.libo.springboot.entity.Admin;
import com.libo.springboot.exception.ServiceException;
import com.libo.springboot.mapper.AdminMapper;
import com.libo.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends ServiceImpl<AdminMapper,Admin> {

    private static final Log LOG = Log.get();

    public boolean saveAdmin(Admin admin) {
        return saveOrUpdate(admin);
    }


    public AdminDTO login(AdminDTO adminDTO) {
        Admin one = getAdminInfo(adminDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, adminDTO, true);
            //设置token
            String token = TokenUtils.genToken(one.getId().toString(),one.getPassword());
            adminDTO.setToken(token);
            return adminDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "教师工号或密码错误");
        }
    }

    public Admin register(AdminDTO adminDTO) {
        Admin one = getAdminInfo(adminDTO);
        if (one == null) {
            one = new Admin();
            BeanUtil.copyProperties(adminDTO, one, true);
            save(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    private Admin getAdminInfo(AdminDTO adminDTO) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("jobNumber", adminDTO.getJobNumber());
        queryWrapper.eq("password", adminDTO.getPassword());
        Admin one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }


    /*这里原本是使用mybatis时候要写的代码，后来引用了mybatis plus就给注释了
    @Autowired
    private AdminMapper adminMapper;

    public int save(Admin admin){
        if(admin.getId() == null ){  //如果一开始没有id，证明是新插入的一条数据
            return adminMapper.insert(admin);
        }else {    //否则为更新一条数据
            return adminMapper.update(admin);
        }
    }*/
}
