package com.libo.springboot.controller.dto;

import lombok.Data;

/**
 * 接受前端登录请求的参数
 */
@Data
public class VisitorEnterDTO {
    private Integer id;
    private String name;
    private String idNum;
    private String phone;
    private String campus;
    private String reason;
    private String healthCode;
    private String journeyCode;
    private String acid;
    private String place;
    private String health;
    private float temperature;
    private boolean enable;
    private String createTime;
}
