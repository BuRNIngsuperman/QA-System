package com.example.wenda.Controller;

import com.alibaba.fastjson.JSON;
import com.example.wenda.model.HostHolder;
import com.example.wenda.model.Message;
import com.example.wenda.model.User;
import com.example.wenda.model.ViewObject;
import com.example.wenda.service.MessageService;
import com.example.wenda.service.UserService;
import com.example.wenda.util.JSONUtil;
import org.aspectj.asm.IModelFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/msg/addMessage",method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){

        try{
            if(hostHolder.getUser() == null){
                return JSONUtil.getJSONString(999,"用户未登录");
            }

            User user = userService.getUserByName(toName);
            if (user == null){
                return JSONUtil.getJSONString(1,"用户不存在");

            }

            Message message = new Message();
            message.setContent(content);
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setCreatedDate(new Date());
            messageService.addMessage(message);

            return JSONUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("发送消息失败" + e.getMessage());
            return JSONUtil.getJSONString(1,"发送信息失败");
        }
    }

    @RequestMapping(value = "/msg/list",method = {RequestMethod.GET})
    public String getConversationList(Model model){
        if (hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }

        int userId = hostHolder.getUser().getId();
        List<Message> conversationList = messageService.getConversationList(userId,0,10);
        List<ViewObject> conversations = new ArrayList<>();
        for(Message message: conversationList){
            ViewObject view = new ViewObject();
            view.set("conversation",message);
            int targetId = message.getFromId() == userId ? message.getToId() : message.getFromId();
            view.set("user",userService.getUser(targetId));
            view.set("unread",messageService.getConversationUnreadCount(userId,message.getConversationId()));
            conversations.add(view);
        }

        model.addAttribute("conversations",conversations);

        return "letter";
    }

    @RequestMapping(value = "/msg/detail",method = {RequestMethod.GET})
    public String getConversationDetail(Model model,@RequestParam("conversationId") String conversationId){
        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId,0,10);
            int userId = hostHolder.getUser().getId();
            messageService.updateConversationStatus(userId,conversationId);
            List<ViewObject> messages = new ArrayList<>();
            for(Message message : messageList){
                ViewObject view = new ViewObject();
                view.set("message",message);
                view.set("user",userService.getUser(message.getFromId()));
                messages.add(view);
            }
            model.addAttribute("messages",messages);

        }catch (Exception e){
            logger.error("获取详情失败" + e.getMessage());
        }

        return "letterDetail";
    }

}
