package com.qin.defender.counter.simple;

import com.qin.defender.config.Config;
import com.qin.defender.config.ConfigItem;
import com.qin.defender.counter.ICounter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sebastian
 * @date 2021/5/17
 */
public class SimpleCounter implements ICounter {
    private final Config config = Config.getInstance();

    private static final SimpleCounter instance = new SimpleCounter();

    private final ConcurrentMap<String, AtomicInteger> map = new ConcurrentHashMap<>();
    private final DelayQueue<DelayItem<Pair>> queue = new DelayQueue<>();

    private SimpleCounter() {

        Runnable daemonTask = this::daemonCheck;

        Thread daemonThread = new Thread(daemonTask);
        daemonThread.setDaemon(true);
        daemonThread.setName("Visit Counter Daemon Thread");
        daemonThread.start();
    }

    public static SimpleCounter getInstance() {
        return instance;
    }

    //清除过期计数器
    private void daemonCheck() {
        for (; ; ) {
            try {
                DelayItem<Pair> delayItem = queue.take();
                // 超时对象处理
                Pair pair = delayItem.getItem();
                map.remove(pair.getIp());
            } catch (InterruptedException e) {
                break;
            }
        }
    }


    @Override
    public int secondCount(String ip) {
        int duration = 1;
        for (ConfigItem.Threshold threshold : config.getConfigItem().getThresholds()) {
            if (ConfigItem.Threshold.SECONDS.equals(threshold.getLevel())) {
                duration = threshold.getDuration();
                break;
            }
        }
        return increment(ip, TimeUnit.SECONDS, duration);
    }

    @Override
    public int minuteCount(String ip) {
        int duration = 1;
        for (ConfigItem.Threshold threshold : config.getConfigItem().getThresholds()) {
            if (ConfigItem.Threshold.MINUTES.equals(threshold.getLevel())) {
                duration = threshold.getDuration();
                break;
            }
        }
        return increment(ip, TimeUnit.MINUTES, duration);
    }

    @Override
    public int getSecondCount(String ip) {
        return getCount(ip, TimeUnit.SECONDS);
    }

    @Override
    public int getMinuteCount(String ip) {
        return getCount(ip, TimeUnit.MINUTES);
    }


    private int getCount(String ip, TimeUnit timeUnit) {

        String key = ip + "_" + timeUnit.name();

        AtomicInteger count = map.get(key);
        if (count != null) {
            return count.get();
        }

        return 0;
    }

    private int increment(String ip, TimeUnit timeUnit, int duration) {

        String key = ip + "_" + timeUnit.name();

        AtomicInteger count = map.get(key);

        if (count == null) {
            count = new AtomicInteger(1);
            map.put(key, count);

            queue.add(new DelayItem<>(new Pair(key, count), TimeUnit.NANOSECONDS.convert(duration, timeUnit)));

        } else {
            count.incrementAndGet();
        }

        return count.get();
    }

    public static void main(String[] args) {

        SimpleCounter couter = SimpleCounter.getInstance();
        //couter.increment("192.168.1.101",TimeUnit.SECONDS);
        //couter.increment("192.168.1.101",TimeUnit.SECONDS);
        //couter.increment("192.168.1.101",TimeUnit.SECONDS);

        couter.secondCount("192.168.1.101");
        couter.secondCount("192.168.1.101");
        couter.secondCount("192.168.1.101");


        System.out.println(couter.getSecondCount("192.168.1.101"));

        try {
            Thread.sleep(3050);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(couter.getCount("192.168.1.101", TimeUnit.SECONDS));

    }

}
