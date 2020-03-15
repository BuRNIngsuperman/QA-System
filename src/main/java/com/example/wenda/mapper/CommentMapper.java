package com.example.wenda.mapper;


import com.example.wenda.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface CommentMapper {

    int addComment(Comment comment);

    List<Comment> selectCommentByEntity(@Param("entityId") int entityId,
                                        @Param("entityType") int entityType);

    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType, @Param("status") int status);

    Comment getCommentById(int id);

}
