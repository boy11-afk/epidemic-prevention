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
@TableName(value = "sys_admin")
@ToString
public class Admin {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String college;
    @TableField(value = "jobNumber")  //指定数据库名称
    private String jobNumber;
    @TableField(value = "adminName")  //指定数据库名称
    private String adminName;
    private String password;
    private String phone;
    private String email;
    private String address;
    @TableField(value = "create_time")  //指定数据库名称
    private String createTime;
    @TableField(value = "avatar_url")  //指定数据库名称
    private String avatarUrl;
}
