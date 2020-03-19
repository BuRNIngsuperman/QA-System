package com.example.wenda.service;


import com.example.wenda.mapper.CommentMapper;
import com.example.wenda.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentByEntity(int entityId, int entityType){
        return commentMapper.selectCommentByEntity(entityId,entityType);
    }

    public int addComment(Comment comment){
        //标签和敏感词过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentMapper.addComment(comment);
    }

    public int getCommentCount(int entityId,int entityType){
        return commentMapper.getCommentCount(entityId,entityType);
    }

    public void deleteComment(int entityId,int entutyType,int status){
        commentMapper.updateStatus(entityId,entutyType,1);
    }

    public Comment getCommentById(int id){
        return commentMapper.getCommentById(id);
    }

    public int getCommentCountByUserId(int userId){
        return commentMapper.getCommentCountByuserId(userId);
    }

}
