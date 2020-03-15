package com.example.wenda.async;

import com.alibaba.fastjson.JSONObject;
import com.example.wenda.util.JSONUtil;
import com.example.wenda.util.JedisAdaptor;
import com.example.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Jin Qiuyang
 * @date 2020/3/15
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdaptor jedisAdaptor;

    public boolean fireEvent(EventModel eventModel){
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdaptor.lpush(key,json);

            return true;

        }catch (Exception e){
            return false;
        }
    }
}
