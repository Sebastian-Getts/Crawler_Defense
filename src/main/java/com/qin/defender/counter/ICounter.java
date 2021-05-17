package com.qin.defender.counter;

/**
 * @author Sebastian
 * @date 2021/5/14
 */
public interface ICounter {

    int secondCount(String ip);

    int minuteCount(String ip);

    int getSecondCount(String ip);

    int getMinuteCount(String ip);
}
