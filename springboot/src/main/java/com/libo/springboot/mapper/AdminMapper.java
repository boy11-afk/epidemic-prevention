package com.libo.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.libo.springboot.entity.Admin;



public interface AdminMapper extends BaseMapper<Admin> {

    /*
    @Select("select * from sys_admin")
    List<Admin> findAll();

    @Insert("Insert into sys_admin(college,jobNumber,adminName,password,phone,email,address) VALUES (#{college},#{jobNumber},#{adminName},#{password},#{phone},#{email},#{address})")
    int insert(Admin admin);

    int update(Admin admin);

    @Delete("delete from sys_admin where id = #{id}")
    Integer deleteById(@Param("id") Integer id);

    @Select("select * from sys_admin where adminName like concat('%',#{adminName},'%') limit #{pageNum},#{pageSize}")
    List<Admin> selectPage(Integer pageNum, Integer pageSize,String adminName);

    @Select("select count(*) from sys_admin where adminName like concat('%',#{adminName},'%')")
    Integer selectTotal(String adminName);
    */
}
