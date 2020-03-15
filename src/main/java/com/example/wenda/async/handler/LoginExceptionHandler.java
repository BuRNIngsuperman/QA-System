package com.example.wenda.async.handler;

import com.example.wenda.async.EventHandler;
import com.example.wenda.async.EventModel;
import com.example.wenda.async.EventType;
import com.example.wenda.util.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Qiuyang
 * @date 2020/3/15
 */

@Component
public class LoginExceptionHandler implements EventHandler {

    private final static Logger  logger= LoggerFactory.getLogger(LoginExceptionHandler.class);

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandler(EventModel model) {
        // 判断这个用户登陆异常
        Map<String,Object> map = new HashMap<>();
        map.put("username",model.getExt("username"));

        mailSender.sendWithHTMLTemplate(model.getExt("email"),"登录IP异常","mail/login_exception.html",map);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
