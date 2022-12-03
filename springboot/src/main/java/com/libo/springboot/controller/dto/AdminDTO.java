package com.libo.springboot.controller.dto;


import lombok.Data;

/**
 * 接受前端登录请求的参数
 */
@Data
public class AdminDTO {
    private String jobNumber;
    private String password;
    private String adminName;
    private String avatar_url;
    private String token;
}
