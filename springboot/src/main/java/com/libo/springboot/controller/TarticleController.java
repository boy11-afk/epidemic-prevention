package com.libo.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.libo.springboot.common.Result;
import com.libo.springboot.entity.Article;
import com.libo.springboot.entity.Tarticle;
import com.libo.springboot.service.ArticleService;
import com.libo.springboot.service.TarticleService;
import com.libo.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/tarticle")
public class TarticleController {
    @Resource
    private TarticleService tarticleService;

    // 新增或者更新
    @PostMapping
    public boolean save(@RequestBody Tarticle tarticle) {
        if (tarticle.getId() == null) { // 新增
            tarticle.setTime(DateUtil.now());  // new Date()
            tarticle.setPublisher(TokenUtils.getCurrentAdmin().getAdminName());
        }

        return tarticleService.saveOrUpdate(tarticle);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {

        return tarticleService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return  tarticleService.removeByIds(ids);
    }

    @GetMapping
    public List<Tarticle> findAll() {
        return tarticleService.list();
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(tarticleService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String title,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Tarticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (StrUtil.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        return Result.success(tarticleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
