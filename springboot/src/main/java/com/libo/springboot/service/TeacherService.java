package com.libo.springboot.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.common.Constants;
import com.libo.springboot.controller.dto.TeacherDTO;
import com.libo.springboot.entity.Teacher;
import com.libo.springboot.exception.ServiceException;
import com.libo.springboot.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

@Service
public class TeacherService extends ServiceImpl<TeacherMapper, Teacher> {

    private static final Log LOG = Log.get();

    public TeacherDTO login(TeacherDTO teacherDTO) {
        Teacher one = getTeacherInfo(teacherDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, teacherDTO, true);
            return teacherDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "教师工号或密码错误");
        }
    }

    private Teacher getTeacherInfo(TeacherDTO teacherDTO) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("jobNumber", teacherDTO.getJobNumber());
        queryWrapper.eq("password", teacherDTO.getPassword());
        Teacher one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    public boolean saveTeacher(Teacher teacher) {

        return saveOrUpdate(teacher);
    }
}
