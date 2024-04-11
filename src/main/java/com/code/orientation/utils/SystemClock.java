package com.code.orientation.utils;

import com.code.orientation.exception.CustomException;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 缓存时间戳解决System.currentTimeMillis()高并发下性能问题<br/>
 *     问题根源分析: <a href="http://pzemtsov.github.io/2017/07/23/the-slow-currenttimemillis.html">...</a>
 *
 **/
public class SystemClock {

    private final long period;
    private final AtomicLong now;

    private SystemClock(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    /**
     * 获取当前时间戳
     *
     * @return long
     */
    public static long now() {
        return getInstance().now.get();
    }

    /**
     * 获取实例对象
     *
     * @return {@link SystemClock}
     */
    private static SystemClock getInstance() {
        return SystemClockHolder.INSTANCE;
    }

    /**
     * 起一个线程定时刷新时间戳
     */
    private void scheduleClockUpdating() {
        ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(
                1,
                runnable -> {
                    Thread thread = new Thread(runnable, "System Clock");
                    thread.setDaemon(true);
                    return thread;
                }
        );

        scheduler.scheduleWithFixedDelay(() -> {
            try {
                now.set(System.currentTimeMillis());
            } catch (Exception e) {
                throw new CustomException(e.getMessage());
            }
        }, 0, period, TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::shutdown));
    }

    private static class SystemClockHolder {
        private static final SystemClock INSTANCE = new SystemClock(1);
    }
}

