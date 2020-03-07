package com.example.wenda.Controller;

import com.example.wenda.model.HostHolder;
import com.example.wenda.model.Question;
import com.example.wenda.model.ViewObject;
import com.example.wenda.service.QuestionService;
import com.example.wenda.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.plugin.javascript.navig.LinkArray;

import javax.jws.WebParam;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;


    @RequestMapping(path = {"/","/index"},method = {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model){
        List<ViewObject> views = getViewsList(0, 0, 10);
        model.addAttribute("views",views);
        return "index";
    }


    @RequestMapping(value = "/user/{userId}",method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(@PathVariable("userId") int userId,
                            Model model){
        List<ViewObject> views = getViewsList(userId, 0, 10);
        model.addAttribute("views",views);

        return "index";
    }

    private List<ViewObject> getViewsList(int userId,int offset,int limit){
        List<Question> lastedQuestion = questionService.getLastedQuestion(userId, offset, limit);
        List<ViewObject> views = new ArrayList<>();
        for(Question question : lastedQuestion){
            ViewObject view = new ViewObject();
            view.set("question",question);
            view.set("user",userService.getUser(question.getUserId()));
            views.add(view);
        }
        return views;


    }
}
