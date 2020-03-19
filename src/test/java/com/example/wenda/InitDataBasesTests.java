package com.example.wenda;


import com.example.wenda.mapper.QuestionMapper;
import com.example.wenda.mapper.UserMapper;
import com.example.wenda.model.EntityType;
import com.example.wenda.model.Question;
import com.example.wenda.model.User;
import com.example.wenda.service.FollowService;
import com.example.wenda.util.JedisAdaptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.ws.FaultAction;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class InitDataBasesTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    JedisAdaptor jedisAdaptor;

    @Autowired
    FollowService followService;


    @Test
    public void contextLoads() {
        Random random = new Random();
        jedisAdaptor.getJedis().flushDB();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i+1));
            user.setPassword("");
            user.setSalt("");
            userMapper.addUser(user);

            //互相关注
            for (int j = 1; j < i; ++j) {
                followService.follow(j, EntityType.ENTITY_USER, i);
            }

            user.setPassword("newpassword");
            userMapper.updatePassword(user);

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("Balaababalalalal Content %d", i));
            questionMapper.addQuestion(question);
        }

        Assert.assertEquals("newpassword", userMapper.selectById(1).getPassword());
    }
}
