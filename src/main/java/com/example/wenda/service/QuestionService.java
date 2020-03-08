package com.example.wenda.service;

import com.example.wenda.mapper.QuestionMapper;
import com.example.wenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.swing.text.html.HTML;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    SensitiveService sensitiveService;

    public List<Question> getLastedQuestion(int userId,int offset,int limit){
        return questionMapper.selectLatestQuestions(userId,offset,limit);
    }

    public int addQuestion(Question question){
        //敏感词过滤
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));

        question.setContent(sensitiveService.filter(question.getContent()));
        question.setTitle(sensitiveService.filter(question.getTitle()));
        return questionMapper.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public Question getQuestionById(int id){
        return questionMapper.getQuestionById(id);
    }

    public int updateCommentCount(int id,int commentCount){

        return questionMapper.updateCommentCount(id,commentCount);
    }
}
