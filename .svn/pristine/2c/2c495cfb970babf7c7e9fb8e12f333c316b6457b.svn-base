package org.shoper.concurrent;

import org.junit.Before;
import org.junit.Test;
import org.shoper.commons.DateUtil;
import org.shoper.concurrent.collection.ConcurrentSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class ConcurrentSet_Test
{
	ConcurrentSet<String> cset = null;
	@Before
	public void init()
	{
		cset = new ConcurrentSet<>();
	}
	@Test
	public void performance_add_test() throws Exception
	{

		int p = 5;
		ExecutorService service = Executors.newFixedThreadPool(p);
		CountDownLatch cdl = new CountDownLatch(p);
		List<Future<Integer>> futureTasks = new ArrayList();
		for (int i = 0; i < p; i++)
		{
			final int c = i;
			futureTasks.add(service.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception
				{
					int avg = 0;
					long s1 = System.currentTimeMillis();
					for (int j = 0; j < 200000; j++)
					{
						cset.add(j + "" + c + "");
					}
					long s2 = System.currentTimeMillis();
					System.out.println(
							200000 + "条数据耗时:" + DateUtil.TimeToStr(s2 - s1));
					avg += s2 - s1;
					cdl.countDown();
					return avg;
				}
			}));
		}
		cdl.await();
		int avg = 0;
		for (Future<Integer> future : futureTasks)
		{
			avg += future.get();
		}
		service.shutdown();
		System.out.println("平均耗时:" + avg / 3);
	}
}
