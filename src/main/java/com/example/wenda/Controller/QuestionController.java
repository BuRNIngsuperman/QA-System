package com.example.wenda.Controller;

import com.example.wenda.model.*;
import com.example.wenda.service.*;
import com.example.wenda.util.JSONUtil;
import com.example.wenda.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);


    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

    @RequestMapping(value = "/question/add",method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content){
        try {
            Question question = new Question();
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setTitle(title);
            if (hostHolder.getUser() == null) {
//                return JSONUtil.getJSONString(999);
                question.setUserId(JSONUtil.ANONYMOUS_USERID);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question) > 0) {
                return JSONUtil.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }
        return JSONUtil.getJSONString(1, "失败");

    }


    @RequestMapping("/question/{qid}")
    public String questionDetail(@PathVariable("qid") int qid,
                                 Model model){

        Question question = questionService.getQuestionById(qid);
        model.addAttribute("question",question);
        List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> views = new ArrayList<>();
        for(Comment comment : commentList){
            ViewObject view = new ViewObject();
            view.set("comment",comment);

            if (hostHolder.getUser() == null) {
                view.set("liked", 0);
            } else {
                view.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            view.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            view.set("user",userService.getUser(comment.getUserId()));
            views.add(view);
        }

        model.addAttribute("comments",views);

        List<ViewObject> followUsers = new ArrayList<ViewObject>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }
        return "detail";
    }
}
