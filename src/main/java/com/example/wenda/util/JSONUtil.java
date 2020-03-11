package com.example.wenda.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONUtil {
    private static final Logger logger = LoggerFactory.getLogger(JSONUtil.class);

    public static int ANONYMOUS_USERID = 3;


    public static String getJSONString(int code){
        JSONObject json = new JSONObject();
        json.put("code",code);
        return json.toJSONString();
    }

    public static String getJSONString(int code,String msg){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        return json.toJSONString();
    }
}