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
    User getById(@Param("id") Long id);

    /**
     * 根据name查找user表里的记录
     * @param nickname
     * @return
     */
    @Select("select * from user where nickname=#{nickname}")
    User getByName(@Param("nickname") String nickname);

}
