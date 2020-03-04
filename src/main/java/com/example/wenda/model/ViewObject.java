package com.example.wenda.model;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 视图融合类
 */
public class ViewObject {

    private Map<String,Object> view = new HashMap<>();

    public void set(String key,Object value){
        view.put(key,value);
    }

    public Object get(String key){
        return view.get(key);
    }

}
