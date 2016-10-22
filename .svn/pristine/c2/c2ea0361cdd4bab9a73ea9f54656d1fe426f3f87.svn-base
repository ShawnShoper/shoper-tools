package org.shoper.redis;

import org.shoper.commons.ObjectUtil;
import org.shoper.commons.StringUtil;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.List;

/**
 * Redis客户端...
 * 
 * @author ShawnShoper
 *
 */
public class RedisClient
{
	private Jedis jedis;
	public RedisClient(Jedis jedis)
	{
		this.jedis = jedis;
	}
	public RedisClient(String host, int port, String password)
	{
		Jedis jedis = new Jedis(host, port);
		if (password != null)
		{
			jedis.auth(password);
		}
		this.jedis = jedis;
	}
	public RedisClient(String host, int port)
	{
		this(host, port, null);
	}
	/**
	 * push to the queue 's tail
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public long tpush(String key, String value)
	{
		return jedis.rpush(key, value);
	}
	/**
	 * push to the queue's head
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public long hpush(String key, String value)
	{
		return jedis.lpush(key, value);
	}
	/**
	 * pop the head of the queue
	 * 
	 * @param key
	 * @return
	 */
	public String hpop(String key)
	{
		return jedis.lpop(key);
	}
	/**
	 * pop the tail of the queue
	 * 
	 * @param key
	 * @return
	 */
	public String tpop(String key)
	{
		return jedis.rpop(key);
	}
	/**
	 * pop the tail of the queue ,block if
	 * 
	 * @param key
	 * @return
	 */
	public String brpop(String key)
	{

		return brpop(0, key);
	}
	/**
	 * pop the tail of the queue ,block if result is null
	 * 
	 * @param key
	 *            key
	 * @param time
	 *            wait time
	 * @return
	 */
	public String brpop(int time, String key)
	{
		List<String> result = jedis.brpop(time, key);
		if (result == null || result.isEmpty())
		{
			return null;
		}
		return result.get(1);
	}
	/**
	 * 缓存一段时间数据<br>
	 * Created by ShawnShoper 2016年5月19日
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 */
	public void setex(String key, String value, int expire)
	{
		jedis.setex(key, expire, value);
	}
	public String get(String key)
	{
		return jedis.get(key);
	}
	public <K> K getObject(byte[] key, Class<K> clazz)
	{
		return ObjectUtil.deserialize(jedis.get(key), clazz);
	}
	public byte[] getBytes(byte[] key)
	{
		return jedis.get(key);
	}
	/**
	 * 缓存一段时间数据<br>
	 * Created by ShawnShoper 2016年5月19日
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 */
	public void setex(byte[] key, byte[] value, int expire)
	{
		jedis.setex(key, expire, value);
	}
	/**
	 * 缓存一段时间 Serializable 对象<br>
	 * Created by ShawnShoper 2016年5月19日
	 * 
	 * @param key
	 *            存储 key
	 * @param obj
	 *            实现 Serializable 对象
	 * @param expire
	 */
	public void setex(String key, Serializable obj, int expire)
	{
		if (StringUtil.isNull(key) || StringUtil.isNull(obj))
			throw new NullPointerException();
		setex(key.getBytes(), ObjectUtil.serialize(obj), expire);
	}
	/**
	 * close the redis connection
	 */
	public void close()
	{
		jedis.close();
	}
	protected Jedis getJedis()
	{
		return jedis;
	}
}
