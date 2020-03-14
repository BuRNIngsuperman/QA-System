package com.example.wenda.service;

import com.example.wenda.util.JedisAdaptor;
import com.example.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jin Qiuyang
 * @date 2020/3/14
 */

@Service
public class LikeService {

    @Autowired
    JedisAdaptor jedisAdaptor;

    public long like(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdaptor.sadd(likeKey,String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisAdaptor.srem(disLikeKey,String.valueOf(userId));

        return jedisAdaptor.scard(likeKey);
    }

    public long disLike(int userId,int entityType,int entityId){
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisAdaptor.sadd(disLikeKey,String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdaptor.srem(likeKey,String.valueOf(userId));

        return jedisAdaptor.scard(likeKey);
    }

    public long getLikeStatus(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        if(jedisAdaptor.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);

        return jedisAdaptor.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }

    public long getLikeCount(int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        return jedisAdaptor.scard(likeKey);
    }
}
