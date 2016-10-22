package org.shoper.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Closeable;

/**
 * Redis connection pool
 * 
 * @author ShawnShoper
 *
 */
public class RedisPool implements Closeable
{
	private volatile JedisPool jedisPool = null;
	public static void init()
	{

	}
	/**
	 * 获取redisClient
	 * 
	 * @return
	 */
	public RedisClient getRedisClient()
	{
		Jedis jedis = jedisPool.getResource();
		RedisClient redisClient = new RedisClient(jedis);
		return redisClient;
	}
	private RedisPool()
	{
	}
	/**
	 * 初始化Redis pool
	 * 
	 * @param host
	 * @param port
	 * @param timeout
	 * @param password
	 * @return
	 */
	public static RedisPool newInstances(String host, int port, int timeout,
										 String password)
	{
		RedisPool redisPool = new RedisPool();
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		if (redisPool.jedisPool == null)
			redisPool.jedisPool = new JedisPool(config, host, port, timeout,
					password);
		return redisPool;
	}
	/**
	 * close redis pool
	 */
	@Override
	public void close()
	{
		jedisPool.close();
	}
}
