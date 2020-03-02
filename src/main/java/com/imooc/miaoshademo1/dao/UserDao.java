package com.imooc.miaoshademo1.dao;

import com.imooc.miaoshademo1.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author w1586
 * @Date 2020/3/3 0:15
 * @Cersion 1.0
 */
@Mapper
public interface UserDao {

    /**
     * 根据id查找user表里的记录
     * @param id
     * @return
     */
    @Select("select * from user where id=#{id}")
    public User getById(@Param("id") int id);

}
