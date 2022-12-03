package com.libo.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.entity.Report;
import com.libo.springboot.mapper.ReportMapper;
import org.springframework.stereotype.Service;

@Service
public class ReportService extends ServiceImpl<ReportMapper, Report> {

    public boolean saveReport(Report report){
        return saveOrUpdate(report);
    }

}
