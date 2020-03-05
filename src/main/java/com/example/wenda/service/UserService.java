package com.example.wenda.service;


import com.example.wenda.mapper.LoginTicketMapper;
import com.example.wenda.mapper.UserMapper;
import com.example.wenda.model.LoginTicket;
import com.example.wenda.model.User;
import com.example.wenda.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    public User getUser(int id){
        return userMapper.selectById(id);
    }

    /**
     * 用户注册服务
     * @param userName
     * @param password
     * @return
     */
    public Map<String,String> register(String userName,String password){
        Map<String,String> map = new HashMap<>();

        //判断用户名不能为空
        if(StringUtils.isBlank(userName)){
            map.put("msg","用户名不能为空");
            return map;
        }
        //判断密码不能为空
        if (StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        //判断用户名长度
        if (userName.length() > 10){
            map.put("msg","用户名长度不能超过10");
            return map;
        }
        //判断密码长度
        if (password.length() < 6 || password.length() > 20){
            map.put("msg","密码长度要在6到20之间");
            return map;
        }

        //判断用户名是否存在
        User user = userMapper.selectByName(userName);
        if (user != null){
            map.put("msg","用户名已经存在");
            return map;
        }

        //注册用户
        user = new User();
        user.setName(userName);
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(MD5Util.MD5(password + user.getSalt()));
        userMapper.addUser(user);


        return map;

    }


    /**
     * 用户登录服务
     * @param userName
     * @param password
     * @return
     */
    public Map<String,String> login(String userName,String password){

        Map<String,String> map = new HashMap<>();

        //判断用户名不能为空
        if(StringUtils.isBlank(userName)){
            map.put("msg","用户名不能为空");
            return map;
        }
        //判断密码不能为空
        if (StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        //判断用户名长度
        if (userName.length() > 10){
            map.put("msg","用户名长度不能超过10");
            return map;
        }
        //判断密码长度
        if (password.length() < 6 || password.length() > 20){
            map.put("msg","密码长度要在6到20之间");
            return map;
        }

        //判断用户名是否存在
        User user = userMapper.selectByName(userName);
        if (user == null){
            map.put("msg","用户名不存在");
            return map;
        }

        //判断密码正确
        if(!MD5Util.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","用户密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;

        }

    /**
     * 给登录添加token验证
     * @param userId
     * @return
     */
    private String addLoginTicket(int userId){
            LoginTicket loginTicket = new LoginTicket();
            loginTicket.setUserId(userId);
            loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*24);
            loginTicket.setExpired(date);
            loginTicket.setStatus(0);
            loginTicketMapper.addTicket(loginTicket);

            return loginTicket.getTicket();
        }

}


