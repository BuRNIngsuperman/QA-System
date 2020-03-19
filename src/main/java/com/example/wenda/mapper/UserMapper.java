package com.example.wenda.mapper;


import com.example.wenda.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface UserMapper {

    /**
     * 添加用户
     * @param user
     */
    void addUser(User user);


    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User selectById(int id);

    /**
     * 根据用户名查询用户
     * @param name
     * @return
     */
    User selectByName(String name);

    /**
     * 更改密码
     *
     */
    void updatePassword(User user);

    /**
     * 根据id删除用户
     * @param id
     */
    void deleteUserById(int id);

}
