package com.example.wenda.Controller;


import com.example.wenda.model.Question;
import com.example.wenda.service.QuestionService;
import com.example.wenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * 登录注册
     * @return
     */
    @RequestMapping(value = "/reglogin",method = {RequestMethod.GET,RequestMethod.POST})
    public String login(){
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
                            @RequestParam("username") String username,
                           @RequestParam("password") String password){

        try {
            Map<String,String> regMap = userService.register(username,password);

            if(regMap.containsKey("msg")){
                model.addAttribute("msg",regMap.get("msg"));
                return "login";
            }

            return "redirect:/";
        }catch (Exception e){

            logger.error("注册异常: "+ e.getMessage());

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
                        @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                        HttpServletResponse response){


        try {
            Map<String,String> regMap = userService.login(username,password);

            if(regMap.containsKey("ticket")){
                //添加token cookie信息
                Cookie cookie = new Cookie("ticket",regMap.get("ticket").toString());
                cookie.setPath("/");

                response.addCookie(cookie);
                return "redirect:/";
            }else {
                model.addAttribute("msg",regMap.get("msg"));
                return "login";
            }
        }catch (Exception e){

            logger.error("注册异常: "+ e.getMessage());
            return "login";
        }


    }

}
