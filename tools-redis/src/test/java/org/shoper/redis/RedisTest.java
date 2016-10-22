package org.shoper.redis;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RedisTest
{
	RedisClient redisClient;
	@Before
	public void init()
	{
		redisClient = RedisPool
				.newInstances("192.168.100.45", 6379, 10000, "daqsoft")
				.getRedisClient();
	}
	@Test
	public void hpush()
	{
		redisClient.hpush("task-message", "aaaa");
	}
	@Test
	public void brpop()
	{
			System.out.println(redisClient.hpop("task-message"));
	}
	@Test
	public void set()
	{
		redisClient.getJedis().setex("test", Integer.MAX_VALUE,
				"test redis cache time.2016-05-31 10:40:56.expire time"
						+ Integer.MAX_VALUE);
	}
	@Test
	public void setBytes()
	{
		TestA testA = new TestA();
		testA.getDatas().add("xxxx");
		redisClient.getJedis().setex("1".getBytes(), 1000,
									 SerializationUtils.serialize(testA));
	}
	@Test
	public void getBytes()
	{
		TestA testA = (TestA) SerializationUtils
				.deserialize(redisClient.getJedis().get("1".getBytes()));
		System.out.println(testA.getDatas().get(0));
	}
	public static void main(String[] args)
	{
		TestA testA = new TestA();
		testA.getDatas().add("xxxx");
		System.out.println(SerializationUtils.serialize(testA));
	}
	@Test
	public void get()
	{
		System.out.println(redisClient.getJedis().get("1"));
	}
	@After
	public void destroy()
	{
		redisClient.close();
	}
	static class TestA implements Serializable
	{
		private List<String> datas = new ArrayList<>();

		public List<String> getDatas()
		{
			return datas;
		}

		public void setDatas(List<String> datas)
		{
			this.datas = datas;
		}

	}
}
