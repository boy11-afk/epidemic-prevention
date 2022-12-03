package com.libo.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.entity.Tout;
import com.libo.springboot.mapper.ToutMapper;
import org.springframework.stereotype.Service;

@Service
public class ToutService extends ServiceImpl<ToutMapper, Tout> {
    public boolean saveTout(Tout tout) {

        return saveOrUpdate(tout);
    }
}
