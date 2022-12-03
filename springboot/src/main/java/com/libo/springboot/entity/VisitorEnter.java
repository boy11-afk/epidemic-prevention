package com.libo.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data//省去了写Set和Get方法
@NoArgsConstructor//省去了创建无参构造
@AllArgsConstructor//省去了创建有参构造
@TableName(value = "sys_visitorenter")
@ToString
public class VisitorEnter {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    @TableField(value = "idNum")  //指定数据库名称
    private String idNum;
    private String phone;
    private String campus;
    private String reason;
    @TableField(value = "healthCode")  //指定数据库名称
    private String healthCode;
    @TableField(value = "journeyCode")  //指定数据库名称
    private String journeyCode;
    private String acid;
    private String place;
    private String health;
    private float temperature;
    private boolean enable = true;
    @TableField(value = "createTime")  //指定数据库名称
    private String createTime;
}
