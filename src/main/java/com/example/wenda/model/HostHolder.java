package com.example.wenda.model;


import org.springframework.stereotype.Component;

@Component
public class HostHolder {

    /**
     * 每个线程独占一份
     */
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
