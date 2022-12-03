package com.libo.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.entity.Out;
import com.libo.springboot.mapper.OutMapper;
import org.springframework.stereotype.Service;

@Service
public class OutService extends ServiceImpl<OutMapper, Out> {

    public boolean saveOut(Out out) {

        return saveOrUpdate(out);
    }
}
