package org.shoper.redis;

import java.util.concurrent.TimeUnit;

public class ConcurrentTest {
    RedisPool redisPool = RedisPool.newInstances("192.168.100.45", 6379, 10000,
                                                 "daqsoft"
    );

    public static void main(String[] args) throws InterruptedException {

        ConcurrentTest concurrentTest = new ConcurrentTest();
        Thread comsumer = new Thread(() ->
                                     {
                                         concurrentTest.comsumer();
                                     });
        comsumer.start();
        for (int i = 0; i < 100; i++) {
            Thread producer = new Thread(() ->
                                         {
                                             concurrentTest.producer();
                                         });
            producer.start();
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    void producer() {
        RedisClient redisClient = redisPool.getRedisClient();
        redisClient.setex("test-1", "10010101010101", 3);
        redisClient.close();
    }

    void comsumer() {
        for (; ; ) {
            RedisClient redisClient = redisPool.getRedisClient();
            try {
                System.out.println(redisClient.get("test-1"));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                redisClient.close();
            }
        }
    }
}
