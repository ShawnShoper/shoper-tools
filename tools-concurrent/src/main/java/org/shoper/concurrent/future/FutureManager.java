package org.shoper.concurrent.future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Task manager 线程池管理器 服务端所有线程的创建销毁全部依赖于该类。
 *
 * @author ShawnShoper
 */
public class FutureManager {
	private static Logger log = LoggerFactory.getLogger(FutureManager.class);

	// 静态区，设置线程池中所有的异常统一捕获
	static {
		try {
			Thread.setDefaultUncaughtExceptionHandler(
					new UncaughtExceptionHandler() {
						@Override
						public void uncaughtException (Thread t, Throwable e) {
							log.error(
									"Thread name '{}' happend exception ...{}",
									t.getName(), e.getMessage()
							);
							e.printStackTrace();
						}
					});
			DEFAULT_GROUP = "default";
			futures = new ConcurrentHashMap<String, Future<?>>();
			groupFuturesPool = new ConcurrentHashMap<String, List<String>>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void init () {
		service = Executors.newCachedThreadPool();
		pushFuture(FutureManager.class.getName(), "futureAutoGC",
				   new AsynCallable<Object, Object>() {
					   @Override
					   public Object run (Object object) throws Exception {
						   if (log.isDebugEnabled())
							   log.debug("Start future manager auto GC...");
						   for (; ; ) {
							   try {
								   for (String group : groupFuturesPool.keySet()) {
									   for (String futureName : groupFuturesPool
											   .get(group)) {
										   Future<?> future = futures
												   .get(futureName);
										   if (future.isCancelled()
												   || future.isDone())
											   removeFuture(group, futureName);
									   }
								   }
								   try {
									   TimeUnit.MINUTES.sleep(1);
								   } catch (InterruptedException e) {
									   // 这里必须捕获-
									   // -避免由于不使用FutureManager时，释放程序后依然无法退出程序...
									   Thread.currentThread().interrupt();
									   return null;
								   }
							   } catch (Exception e) {
								   e.printStackTrace();
							   }
						   }
					   }
				   }
		);
	}

	/**
	 * Task thread container 任务管理池
	 */
	private static ConcurrentHashMap<String, Future<?>> futures;
	private static ConcurrentMap<String, List<String>> groupFuturesPool;
	private static String DEFAULT_GROUP;
	/**
	 * Thread pool .Cached. 线程池
	 */
	private static ExecutorService service;

	/**
	 * Create a new thread and put it in thread container for manager
	 * 获取一个可返回结果的线程，并执行。
	 *
	 * @param name
	 * 		线程ID
	 * @param runnable
	 * 		处理的详情回调。
	 * @return
	 */
	public static <T, K> Future<T> pushFuture (String name,
											   AsynCallable<T, K> runnable) {
		return pushFuture(null, name, runnable);
	}

	/**
	 * @param group
	 * @param name
	 * @param runnable
	 * @return
	 */
	public static <T, K> Future<T> pushFuture (String group, String name,
											   AsynCallable<T, K> runnable) {
		if (service == null)
			init();
		Future<T> future = service.submit(runnable);
		// 创建一个新的线程并放到线程管理池中
		putNewFuture(group, name, future);
		return future;
	}

	public static Future<?> pushFuture (String group, String name,
										AsynRunnable runnable) {
		if (service == null)
			init();
		Future<?> future = service.submit(runnable);
		// 创建一个新的线程并放到线程管理池中
		putNewFuture(group, name, future);
		return future;
	}

	/**
	 * get a future if exists
	 *
	 * @param group
	 * @param name
	 * @return
	 */
	public static Future<?> getFuture (String group, String name) {
		group = group == null ? DEFAULT_GROUP : group;
		String key = group + "-" + name;
		if (futures.containsKey(key)) {
			return futures.get(key);
		}
		return null;
	}

	/**
	 * put thread into thread container 放入一个线程在线程管理池
	 *
	 * @param group
	 * @param name
	 */
	private static synchronized void putNewFuture (String group, String name,
												   Future<?> future) {
		group = group == null ? DEFAULT_GROUP : group;
		if (!futures.containsKey(name)) {
			futures.put(group + "-" + name, future);
			putGroups(group, name);
		}
	}

	private static synchronized void putGroups (String groupName, String name) {
		if (null == name)
			throw new NullPointerException("Future name can not be null");
		if (log.isDebugEnabled())
			log.debug(
					"Put a new future to thread pool,future name is {}",
					name
			);
		if (!groupFuturesPool.containsKey(groupName)) {
			List<String> futureNames = groupFuturesPool.get(DEFAULT_GROUP);
			if (futureNames == null) {
				futureNames = new ArrayList<String>();
			}
			if (futureNames.contains(name)) {
				throw new RuntimeException("The future name '" + name
												   + "' on the group '" + groupName + "' is exists....");
			} else {
				futureNames.add(name);
			}
		}
	}

	/**
	 * Remove thread from threads 通过任务删除一个线程管理池的线程
	 *
	 * @param id
	 * 		任务ID
	 */
	private static synchronized void removeFuture (String group, String id) {
		if (null == id)
			throw new NullPointerException("Future id can not be null");
		group = group == null ? DEFAULT_GROUP : group;
		if (log.isDebugEnabled())
			log.debug("Remove a new future to thread pool,future id is {}", id);
		if (futures.containsKey(group + "-" + id)) {
			Future<?> f = futures.get(group + "-" + id);
			if (!f.isCancelled() || !f.isDone()) {
				f.cancel(true);
			}
			futures.remove(id);
		}
		if (groupFuturesPool.containsKey(group)) {
			List<String> futureList = groupFuturesPool.get(group);
			for (String future : futureList) {
				if (future.equals(id)) {
					futureList.remove(future);
					break;
				}
			}
		}

	}

	/**
	 * Return the future... 线程执行完毕后调用退回线程。
	 *
	 * @param id
	 * 		线程ID
	 */
	public static synchronized void futureDone (String groupName, String id) {
		removeFuture(groupName, id);
	}

	public static synchronized void futureDone (String id) {
		futureDone(DEFAULT_GROUP, id);
	}

	/**
	 * Stopping future... 通过线程ID，停止指定的线程
	 *
	 * @param id
	 * 		线程ID
	 */
	public static synchronized int stopFuture (String group, String id) {
		if (log.isDebugEnabled())
			log.debug(
					"Stop a future and remove future from future pool,future id is {}",
					id
			);
		group = group == null || group.isEmpty() ? DEFAULT_GROUP : group;
		Future<?> future = futures.get(group + "-" + id);
		if (future != null) {
			if (!future.isCancelled() && !future.isDone())
				future.cancel(true);
			futureDone(group, id);
			return 0;
		} else {
			return 1;
		}
	}

	public static synchronized int stopFuture (String group) {
		if (log.isDebugEnabled())
			log.debug(
					"Stop a future and remove future from future pool,future group is {}",
					group
			);
		group = group == null || group.isEmpty() ? DEFAULT_GROUP : group;
		final String groupQ = group;
		futures.forEach((k, v) -> {
			if (k.startsWith(groupQ + "-")) {
				String[] kv = k.split("-");
				if (v != null) {
					if (!v.isCancelled() && !v.isDone())
						v.cancel(true);
					futureDone(kv[0], kv[1]);
				}
			}
		});
		return 0;

	}

	/**
	 * Checking future is alive... 检查线程是否存活状态..
	 *
	 * @param id
	 * 		线程ID
	 * @return true 存活状态，false非存活状态
	 */
	public static synchronized boolean isAlive (String id) {
		Future<?> future = futures.get(id);
		if (future == null) {
			return false;
		}
		return future.isDone() || future.isCancelled() ? false : true;
	}

	public static void close () {
		futures.clear();
		groupFuturesPool.clear();
		service.shutdownNow();
		service = null;
	}
}
