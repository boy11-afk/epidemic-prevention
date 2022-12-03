package com.libo.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.entity.Tarticle;
import com.libo.springboot.mapper.TarticleMapper;
import org.springframework.stereotype.Service;

@Service
public class TarticleService extends ServiceImpl<TarticleMapper, Tarticle> {
}
