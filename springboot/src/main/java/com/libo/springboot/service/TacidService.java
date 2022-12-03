package com.libo.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.entity.Tacid;
import com.libo.springboot.mapper.TacidMapper;
import org.springframework.stereotype.Service;

@Service
public class TacidService extends ServiceImpl<TacidMapper, Tacid> {
    public boolean saveTacid(Tacid tacid) {
        return saveOrUpdate(tacid);
    }
}
