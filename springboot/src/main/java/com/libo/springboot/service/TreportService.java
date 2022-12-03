package com.libo.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.entity.Treport;
import com.libo.springboot.mapper.TreportMapper;
import org.springframework.stereotype.Service;

@Service
public class TreportService extends ServiceImpl<TreportMapper, Treport> {
    public boolean saveReport(Treport treport){
        return saveOrUpdate(treport);
    }
}
