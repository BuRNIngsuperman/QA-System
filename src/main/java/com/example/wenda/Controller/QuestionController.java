package com.example.wenda.Controller;

import com.example.wenda.model.HostHolder;
import com.example.wenda.model.Question;
import com.example.wenda.service.QuestionService;
import com.example.wenda.service.UserService;
import com.example.wenda.util.JSONUtil;
import com.example.wenda.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import java.util.Date;

@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);


    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

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
        model.addAttribute("user",userService.getUser(question.getUserId()));

        return "detail";
    }
}
