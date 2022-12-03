package com.libo.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.entity.Enter;
import com.libo.springboot.mapper.EnterMapper;
import org.springframework.stereotype.Service;

@Service
public class EnterService extends ServiceImpl<EnterMapper, Enter> {

    public boolean saveEnter(Enter enter) {
        return saveOrUpdate(enter);
    }

}
