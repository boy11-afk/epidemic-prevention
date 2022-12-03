package com.libo.springboot.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.common.Constants;
import com.libo.springboot.controller.dto.VisitorEnterDTO;
import com.libo.springboot.controller.dto.VisitorOutDTO;
import com.libo.springboot.entity.VisitorEnter;
import com.libo.springboot.entity.VisitorOut;
import com.libo.springboot.exception.ServiceException;
import com.libo.springboot.mapper.VisitorOutMapper;
import org.springframework.stereotype.Service;

@Service
public class VisitorOutService extends ServiceImpl<VisitorOutMapper, VisitorOut> {

    private static final Log LOG = Log.get();

    public VisitorOutDTO login(VisitorOutDTO visitorOutDTO) {
        VisitorOut one = getVisitorOutInfo(visitorOutDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, visitorOutDTO, true);
            return visitorOutDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "个人信息存在错误");
        }
    }

    private VisitorOut getVisitorOutInfo(VisitorOutDTO visitorOutDTO) {
        QueryWrapper<VisitorOut> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", visitorOutDTO.getName());
        queryWrapper.eq("idNum", visitorOutDTO.getIdNum());
        queryWrapper.eq("phone", visitorOutDTO.getPhone());
        VisitorOut one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    public boolean saveVisitorOut(VisitorOut visitorOut) {
        return saveOrUpdate(visitorOut);
    }
}
