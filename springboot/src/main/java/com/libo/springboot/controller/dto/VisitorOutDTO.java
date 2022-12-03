package com.libo.springboot.controller.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 接受前端登录请求的参数
 */
@Data
public class VisitorOutDTO {
    private Integer id;
    private String name;
    private String idNum;
    private String phone;
    private String campus;
    private String reason;
    private String healthCode;
    private String journeyCode;
    private String health;
    private float temperature;
    private boolean enable;
    private String createTime;
}
