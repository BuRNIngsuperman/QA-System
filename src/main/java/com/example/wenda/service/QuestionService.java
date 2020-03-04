package com.example.wenda.service;

import com.example.wenda.mapper.QuestionMapper;
import com.example.wenda.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    public List<Question> getLastedQuestion(int userId,int offset,int limit){
        return questionMapper.selectLatestQuestions(userId,offset,limit);
    }
}
