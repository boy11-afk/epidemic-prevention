package com.libo.springboot.controller.dto;


import lombok.Data;

/**
 * 接受前端小程序登录请求的参数
 */
@Data
public class StudentDTO {
    private Integer id;
    private String username;
    private String password;
    private String campus;
    private String college;
    private String major;
    private String stuClass;
    private String stuId;
    private String idNum;
    private String phone;
    private String email;
    private String address;
    private String createTime;
}
