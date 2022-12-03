package com.libo.springboot.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.common.Constants;
import com.libo.springboot.controller.dto.VisitorEnterDTO;
import com.libo.springboot.entity.VisitorEnter;
import com.libo.springboot.exception.ServiceException;
import com.libo.springboot.mapper.VisitorEnterMapper;
import org.springframework.stereotype.Service;

@Service
public class VisitorEnterService extends ServiceImpl<VisitorEnterMapper, VisitorEnter> {

    private static final Log LOG = Log.get();

    public VisitorEnterDTO login(VisitorEnterDTO visitorEnterDTO) {
        VisitorEnter one = getVisitorEnterInfo(visitorEnterDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, visitorEnterDTO, true);
            return visitorEnterDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "个人信息存在错误");
        }
    }

    private VisitorEnter getVisitorEnterInfo(VisitorEnterDTO visitorEnterDTO) {
        QueryWrapper<VisitorEnter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", visitorEnterDTO.getName());
        queryWrapper.eq("idNum", visitorEnterDTO.getIdNum());
        queryWrapper.eq("phone", visitorEnterDTO.getPhone());
        VisitorEnter one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    public boolean saveVisitorEnter(VisitorEnter visitorEnter) {
        return saveOrUpdate(visitorEnter);
    }
}
