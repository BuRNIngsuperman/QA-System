package com.example.wenda.service;


import com.example.wenda.mapper.MessageMapper;
import com.example.wenda.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageMapper.addMessage(message) > 0 ? message.getId() : 0;
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageMapper.getConversationDetail(conversationId,offset,limit);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageMapper.getConversationList(userId,offset,limit);
    }

    public int getConversationUnreadCount(int userId,String conversationId){
        return messageMapper.getConversationUnreadCount(userId,conversationId);
    }

    public void updateConversationStatus(int userId,String conversationId){
        messageMapper.updateConversationStatus(userId,conversationId);
    }
}
