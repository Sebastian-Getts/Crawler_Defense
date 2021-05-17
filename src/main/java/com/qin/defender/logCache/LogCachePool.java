package com.qin.defender.logCache;

import com.qin.defender.persistence.LogObject;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Sebastian
 * @date 2021/5/17
 */
public class LogCachePool {
    //缓存池默认大小
    private static final int DEFAULT_SIZE = 1000;

    private final int capability;

    private final List<LogObject> cachedLogs;

    public LogCachePool() {
        this.capability = DEFAULT_SIZE;
        this.cachedLogs = new LinkedList<>();
    }

    public LogCachePool(int capability) {
        this.capability = capability;
        this.cachedLogs = new LinkedList<>();
    }

    public void add(LogObject value) {

        if (this.cachedLogs.size() >= this.capability) {
            //超过容量时，直接持久化
            // todo: insert logObject
        } else {
            this.cachedLogs.add(value);
        }

    }


    public LogObject get(int index) {
        return this.cachedLogs.get(index);
    }

    public LogObject remove(int index) {
        return this.cachedLogs.remove(index);
    }

    public void clear() {
        this.cachedLogs.clear();
    }

    public boolean isEmpty() {
        return cachedLogs.isEmpty();
    }

    public List<LogObject> getCachedLogs() {
        return cachedLogs;
    }

    public int size() {
        return cachedLogs.size();
    }
}
