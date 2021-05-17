package com.qin.defender.logCache;

import com.qin.defender.persistence.IPersistence;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Sebastian
 * @date 2021/5/17
 */
public class CacheManagement {
    private static LogCachePool cachePool = new LogCachePool();

    private static ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

    static {

        //定时将缓存的日志输出到持久化容器
        ses.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {

                try {
                    // todo: 缓存在cachePool中
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 0, 1, TimeUnit.SECONDS);


    }

    public static LogCachePool getLogCachePool() {
        return cachePool;
    }
}
