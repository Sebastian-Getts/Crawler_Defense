package com.qin.defender.counter.simple;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Sebastian
 * @date 2021/5/17
 */
public class DelayItem<T> implements Delayed {

    private static final long NANO_ORIGIN = System.nanoTime();

    static long now() {
        return System.nanoTime() - NANO_ORIGIN;
    }

    private static final AtomicLong sequencer = new AtomicLong(0);

    private final long sequenceNumber;

    private long time = 0;

    private final T item;

    public DelayItem(T submit, long timeout) {
        this.time = time + now();
        this.item = submit;
        this.sequenceNumber = sequencer.getAndIncrement();
    }

    @Override
    public long getDelay(TimeUnit unit) {

        return unit.convert(time - now(), TimeUnit.NANOSECONDS);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int compareTo(Delayed o) {
        if (o == this) {
            return 0;
        }
        if (o instanceof DelayItem) {
            DelayItem item = (DelayItem) o;
            long diff = time - item.time;
            if (diff < 0) {
                return -1;
            } else if (diff > 0) {
                return 1;
            } else if (sequenceNumber < item.sequenceNumber) {
                return -1;
            } else {
                return 1;
            }
        }
        long d = (getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS));

        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    public T getItem() {
        return item;
    }
}
