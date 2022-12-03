package com.libo.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.entity.Article;
import com.libo.springboot.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

@Service
public class ArticleService extends ServiceImpl<ArticleMapper,Article> {
}
