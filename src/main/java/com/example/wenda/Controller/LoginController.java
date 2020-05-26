package com.example.wenda.Controller;


import com.example.wenda.async.EventModel;
import com.example.wenda.async.EventProducer;
import com.example.wenda.async.EventType;
import com.example.wenda.model.HostHolder;
import com.example.wenda.model.Question;
import com.example.wenda.model.User;
import com.example.wenda.service.QuestionService;
import com.example.wenda.service.UserService;
import com.example.wenda.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {

    //日志输出
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    HostHolder hostHolder;

    /**
     * 登录注册
     * @return
     */
    @RequestMapping(value = "/reglogin",method = {RequestMethod.GET,RequestMethod.POST})
    public String reglogin(Model model,
                           @RequestParam(value = "next",required = false) String next){
        model.addAttribute("next",next);
        return "login";
    }

    /**
     * 注册
     * @param model
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/register/",method = {RequestMethod.POST})
    public String register(Model model,
                           @RequestParam(value = "next") String next,
                           @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           HttpServletResponse response){

        try {
            Map<String, Object> map = userService.register(username, password);

            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            logger.error("登陆异常" + e.getMessage());
            return "login";
        }

    }

    /**
     * 用户登录
     * @param model
     * @param username
     * @param password
     * @param rememberme
     * @return
     */
    @RequestMapping(value = "/login/",method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next",required = false) String next,
                        @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){


        try {
            Map<String,Object> logMap = userService.login(username,password);

            if(logMap.containsKey("ticket")){
                //添加token cookie信息
                Cookie cookie = new Cookie("ticket",logMap.get("ticket").toString());
                cookie.setPath("/");

                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);

//                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
//                .setExt("username",username).setExt("email","18949721107@163.com")
//                .setActorId((int)logMap.get("userId")));

                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            }else {
                model.addAttribute("msg",logMap.get("msg"));
                return "login";
            }
        }catch (Exception e){

            logger.error("注册异常: "+ e.getMessage());
            return "login";
        }


    }

    @RequestMapping(value = "/logout",method = {RequestMethod.GET})
    public String login(@CookieValue("ticket") String ticket){
        userService.logout(ticket);

        return "redirect:/";

    }

}
