package com.example.wenda.service;

import com.example.wenda.util.JedisAdaptor;
import com.example.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Jin Qiuyang
 * @date 2020/3/17
 */

@Service
public class FollowService {

    @Autowired
    JedisAdaptor jedisAdaptor;

    public boolean follow(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date = new Date();

        Jedis jedis = jedisAdaptor.getJedis();
        Transaction tx = jedisAdaptor.multi(jedis);

        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));
        tx.zadd(followeeKey,date.getTime(),String.valueOf(entityId));

        List<Object> ret = jedisAdaptor.exec(tx,jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    public boolean unfollow(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);

        Jedis jedis = jedisAdaptor.getJedis();
        Transaction tx = jedisAdaptor.multi(jedis);

        tx.zrem(followerKey,String.valueOf(userId));
        tx.zrem(followeeKey,String.valueOf(entityId));

        List<Object> ret = jedisAdaptor.exec(tx,jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    private List<Integer> getIdFromSet(Set<String> idset){
        List<Integer> ids = new ArrayList<>();
        for(String str:idset){
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    public List<Integer> getFollowers(int entityType, int entityId, int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdFromSet(jedisAdaptor.zrevrange(followerKey,0,count));

    }

    public List<Integer> getFollowers(int entityType, int entityId, int offset,int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdFromSet(jedisAdaptor.zrevrange(followerKey,offset,count));

    }

    public List<Integer> getFollowees(int entityType, int entityId, int count){
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType,entityId);
        return getIdFromSet(jedisAdaptor.zrevrange(followeeKey,0,count));

    }



    public List<Integer> getFollowees(int entityType, int entityId, int offset,int count){
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType,entityId);
        return getIdFromSet(jedisAdaptor.zrevrange(followeeKey,offset,count));
    }

    public long getFollowerCount(int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdaptor.zcard(followerKey);
    }

    public long getFolloweeCount(int userId,int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return jedisAdaptor.zcard(followeeKey);
    }

    public boolean isFollower(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdaptor.zscore(followerKey,String.valueOf(userId)) != null;
    }
}
