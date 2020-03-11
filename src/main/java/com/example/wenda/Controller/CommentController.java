package com.example.wenda.Controller;


import com.alibaba.fastjson.JSON;
import com.example.wenda.model.Comment;
import com.example.wenda.model.EntityType;
import com.example.wenda.model.HostHolder;
import com.example.wenda.model.Question;
import com.example.wenda.service.CommentService;
import com.example.wenda.service.QuestionService;
import com.example.wenda.service.SensitiveService;
import com.example.wenda.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);


    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/addComment",method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            // 过滤content
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                comment.setUserId(JSONUtil.ANONYMOUS_USERID);

            }
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setStatus(0);
            commentService.addComment(comment);

            //更新问题中的评论数量
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            System.out.println(count);
            questionService.updateCommentCount(comment.getEntityId(),count);

            //可以注意一下怎么异步化
        }catch (Exception e){
            logger.error("添加评论失败"  + e.getMessage());
        }

        return "redirect:/question/" + String.valueOf(questionId);

    }
}
