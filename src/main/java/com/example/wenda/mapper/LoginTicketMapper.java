package com.example.wenda.mapper;

import com.example.wenda.model.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface LoginTicketMapper {

    /**
     * 添加ticket
     * @param loginTicket
     */
    void addTicket(LoginTicket loginTicket);


    /**
     * 根据
     * @param ticket
     * @return
     */
    LoginTicket selectByTicket(String ticket);


    void updateStatus(@Param("ticket") String ticket,@Param("status") int status);


}
