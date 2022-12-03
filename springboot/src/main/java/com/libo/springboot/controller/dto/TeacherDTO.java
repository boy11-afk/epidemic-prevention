package com.libo.springboot.controller.dto;

import lombok.Data;

/**
 * 接受前端小程序登录请求的参数
 */
@Data
public class TeacherDTO {
    private Integer id;
    private String campus;
    private String college;
    private String jobNumber;
    private String name;
    private String password;
    private String phone;
    private String email;
    private String address; //这个是办公地址
    private String location;  //这个是家庭住址
    private String createTime;
}
