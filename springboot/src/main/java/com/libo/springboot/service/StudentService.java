package com.libo.springboot.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.libo.springboot.common.Constants;
import com.libo.springboot.controller.dto.StudentDTO;
import com.libo.springboot.entity.Student;
import com.libo.springboot.exception.ServiceException;
import com.libo.springboot.mapper.StudentMapper;
import org.springframework.stereotype.Service;


@Service
public class StudentService extends ServiceImpl<StudentMapper, Student> {
    private static final Log LOG = Log.get();

    public StudentDTO login(StudentDTO studentDTO) {
        Student one = getStuInfo(studentDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, studentDTO, true);
            return studentDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "学号或密码错误");
        }
    }

    private Student getStuInfo(StudentDTO studentDTO) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stuId", studentDTO.getStuId());
        queryWrapper.eq("password", studentDTO.getPassword());
        Student one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    public boolean saveStudent(Student student) {

        return saveOrUpdate(student);
    }

}
