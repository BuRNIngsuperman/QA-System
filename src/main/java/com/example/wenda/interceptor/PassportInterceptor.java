package com.example.wenda.interceptor;

import com.example.wenda.mapper.LoginTicketMapper;
import com.example.wenda.mapper.UserMapper;
import com.example.wenda.model.HostHolder;
import com.example.wenda.model.LoginTicket;
import com.example.wenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/* 拦截器实现用户是谁 有没有权限 */

@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    HostHolder hostHolder;

    /*在controller之前先判断用户信息*/
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if(httpServletRequest.getCookies() != null){
            for(Cookie cookie : httpServletRequest.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if(ticket != null){
            LoginTicket loginTicket = loginTicketMapper.selectByTicket(ticket);
            if(loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0){
                return true;
            }

            User user = userMapper.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null){
            /*这样就可以在view*/
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    /*渲染结束之后清除user信息*/
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
            hostHolder.clear();
    }
}
