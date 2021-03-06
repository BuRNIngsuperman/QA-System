package com.example.wenda.util;

import org.apache.tomcat.util.codec.BinaryDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import javax.swing.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Jin Qiuyang
 * @date 2020/3/14
 */

@Service
public class JedisAdaptor implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdaptor.class);


    private JedisPool pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/1");

    }

    public long sadd(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }

        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Jedis getJedis(){
        return pool.getResource();
    }

    public Transaction multi(Jedis jedis){
        try {
            return jedis.multi();
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }
        return null;
    }

    public List<Object> exec(Transaction tx, Jedis jedis){
        try {
            return tx.exec();
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(tx != null){
                try{
                    tx.close();
                }catch (IOException ioe){
                    logger.error("发生异常" + ioe.getMessage());
                }
            }
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public long zadd(String key,double score,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key,score,value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Set<String> zrange(String key, int start, int end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrange(key,start,end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Set<String> zrevrange(String followerKey, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(followerKey,offset,count);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key,String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key,value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
