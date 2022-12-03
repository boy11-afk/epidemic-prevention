package com.libo.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.entity.Tenter;
import com.libo.springboot.mapper.TenterMapper;
import org.springframework.stereotype.Service;

@Service
public class TenterService extends ServiceImpl<TenterMapper, Tenter> {
    public boolean saveTenter(Tenter tenter) {
        return saveOrUpdate(tenter);
    }
}
