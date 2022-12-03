package com.libo.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.entity.Acid;
import com.libo.springboot.entity.Enter;
import com.libo.springboot.mapper.AcidMapper;
import org.springframework.stereotype.Service;

@Service
public class AcidService extends ServiceImpl<AcidMapper, Acid> {

    public boolean saveAcid(Acid acid) {
        return saveOrUpdate(acid);
    }
}
