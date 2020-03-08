package com.example.wenda.mapper;


import com.example.wenda.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface QuestionMapper {

    String TABLE = " question ";
    String INSERT_FIELDS = " title, content, user_id, created_date, comment_count ";
    /**
     * 根据id删除
     * @param id
     */
    void deleteById(int id);


    /**
     * 添加问题
     * @param question
     */
    @Insert({"insert into ",TABLE,"(",INSERT_FIELDS,
            ") values (#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
    int addQuestion(Question question);

    /**
     * 查找最新的问题
     * @return
     */
    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset, @Param("limit") int limit);


    /**
     * 根据id查询问题
     * @param id
     * @return
     */
    Question getQuestionById(int id);


    /**
     * 更新评论数量
     * @param id
     * @param commentCount
     */
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);


}
