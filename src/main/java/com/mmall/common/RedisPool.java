package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by sww_6 on 2018/10/30.
 */
public class RedisPool {

  private static JedisPool pool;
  private static Integer maxTotal = Integer
      .parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));
  private static Integer maxIdle = Integer
      .parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));

  private static Integer minIdle = Integer
      .parseInt(PropertiesUtil.getProperty("redis.max.total", "redis.min.idle"));

  private static Boolean testOnBorrow = Boolean
      .parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));

  private static Boolean testOnReturn = Boolean
      .parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));
  private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
  private static String redisIp = PropertiesUtil.getProperty("redis.ip");
  private static String redisPassword = PropertiesUtil.getProperty("redis.password");

  private static void initpool() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxTotal(maxTotal);
    jedisPoolConfig.setMaxIdle(maxIdle);
    jedisPoolConfig.setMinIdle(minIdle);
    jedisPoolConfig.setTestOnBorrow(testOnBorrow);
    jedisPoolConfig.setTestOnReturn(testOnReturn);

    jedisPoolConfig.setBlockWhenExhausted(true);
    pool = new JedisPool(jedisPoolConfig, redisIp, redisPort, 1000 * 2, redisPassword);
  }

  static {
    initpool();
  }

  public static Jedis getJedis() {
    return pool.getResource();
  }

  public static void returnResource(Jedis jedis) {
    pool.returnResource(jedis);
  }

  public static void returnBrokenResource(Jedis jedis) {
    pool.returnBrokenResource(jedis);
  }

  public static void main(String[] args) {
    Jedis resource = pool.getResource();
    String set = resource.set("zhang", "yangyang");
    System.out.println(set);
    returnResource(resource);
    pool.destroy();
  }
}
