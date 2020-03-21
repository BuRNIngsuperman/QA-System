package com.example.wenda.async.handler;

import com.example.wenda.async.EventHandler;
import com.example.wenda.async.EventModel;
import com.example.wenda.async.EventType;
import com.example.wenda.model.EntityType;
import com.example.wenda.model.Message;
import com.example.wenda.model.User;
import com.example.wenda.service.MessageService;
import com.example.wenda.service.UserService;
import com.example.wenda.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.soap.SOAPBinding;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Jin Qiuyang
 * @date 2020/3/21
 */
@Component
public class FollowHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;


    @Override
    public void doHandler(EventModel model) {

        Message message = new Message();
        message.setFromId(JSONUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());

        if(model.getEntityId() == EntityType.ENTITY_QUESTION){
            message.setContent("用户" + user.getName()
            + "关注了你的问题");
        }else if(model.getEntityType() == EntityType.ENTITY_USER){
            message.setContent("用户" + user.getName() +
                    "关注了你");
        }

        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
