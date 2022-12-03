package com.libo.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libo.springboot.common.Result;
import com.libo.springboot.entity.Acid;
import com.libo.springboot.entity.Article;
import com.libo.springboot.service.ArticleService;
import com.libo.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    // 新增或者更新
    @PostMapping
    public boolean save(@RequestBody Article article) {
        if (article.getId() == null) { // 新增
            article.setTime(DateUtil.now());  // new Date()
            article.setPublisher(TokenUtils.getCurrentAdmin().getAdminName());
        }

        return articleService.saveOrUpdate(article);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {

        return articleService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return  articleService.removeByIds(ids);
    }

    @GetMapping
    public List<Article> findAll() {
        return articleService.list();
    }


    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(articleService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String title,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (StrUtil.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        return Result.success(articleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
