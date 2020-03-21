package com.example.wenda.service;

import org.springframework.stereotype.Service;

/**
 * @author Jin Qiuyang
 * @date 2020/3/21
 */
@Service
public class WendaService {
    public String getMessage(int userId) {
        return "Hello Message:" + String.valueOf(userId);
    }
}
