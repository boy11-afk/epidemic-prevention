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
@TableName(value = "sys_tacid")
@ToString
public class Tacid {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String campus;
    private String college;
    @TableField(value = "jobNumber")  //指定数据库名称
    private String jobNumber;
    private String name;
    @TableField(value = "idNum")  //指定数据库名称
    private String idNum;
    private String phone;
    private String time;
    private String place;
    @TableField(value = "createTime")  //指定数据库名称
    private String createTime;
}
