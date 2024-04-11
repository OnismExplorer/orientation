package com.code.orientation;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.code.orientation.constants.RedisConstants;
import com.code.orientation.entity.Activity;
import com.code.orientation.service.UserService;
import com.code.orientation.utils.RedisUtil;
import com.code.orientation.utils.SequenceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@SuppressWarnings("all")
class OrientationApplicationTests {

    private static final int THREAD_COUNT = 100; // 线程数
    private static final int TASK_COUNT = 10000; // 每个线程执行的任务数
    private static final Set<Long> set = new HashSet<>();
    /**
     * 重复 id 数量
     */
    private static int repeatd = 0;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        for (int i = 0; i < 50; i++) {
            System.out.println(IdUtil.getSnowflake(1, 1).nextId());
            System.out.println(SequenceUtils.nextId());
        }
    }

    @Test
    void testJson() {
        String str = redisUtil.getString(RedisConstants.ACTIVITY_ENDING.getKey());
        String s = "[{\"content\":\"这是一个活动\",\"count\":0,\"createBy\":1,\"end\":1709628600000,\"gmtCreate\":1709628060610,\"gmtModified\":1709628060611,\"id\":77858152120321,\"isDeleted\":0,\"maxNumber\":200,\"name\":\"活动5\",\"points\":7,\"scoreStandard\":\"本次活动加7分\",\"start\":1709628120000,\"state\":1,\"type\":1,\"updateBy\":1},{\"content\":\"这是一个活动\",\"count\":0,\"createBy\":1,\"end\":1709628600000,\"gmtCreate\":1709628071839,\"gmtModified\":1709628071839,\"id\":77858175188993,\"isDeleted\":0,\"maxNumber\":200,\"name\":\"活动6\",\"points\":7,\"scoreStandard\":\"本次活动加7分\",\"start\":1709628180000,\"state\":1,\"type\":0,\"updateBy\":1},{\"content\":\"这是一个活动\",\"count\":0,\"createBy\":1,\"end\":1709628600000,\"gmtCreate\":1709628087107,\"gmtModified\":1709628087107,\"id\":77858208743425,\"isDeleted\":0,\"maxNumber\":200,\"name\":\"活动6\",\"points\":7,\"scoreStandard\":\"本次活动加7分\",\"start\":1709628240000,\"state\":1,\"type\":1,\"updateBy\":1}]";

        // 解析 JSON 字符串为 JSON 数组
        List<Activity> list = JSON.parseArray(str).toList(Activity.class);
        for (Activity activity : list) {
            System.out.println(activity);
        }
    }

    @Test
    void testMechile() {
        byte[] bytes = new byte[]{2, 3, 4, 5, 6, 7};
        System.out.println(Arrays.toString(bytes));
        System.out.println("-------------------");

        // 将byte数组转换为Base64编码的字符串
        String base64Str = Base64.getEncoder().encodeToString(bytes);

        System.out.println(base64Str);
        System.out.println("-------------------");

        // 缓存
        redisUtil.set("bytes", base64Str, 1000L, TimeUnit.SECONDS);

        // 获取
        String cachedBase64Str = redisUtil.getString("bytes");
        // 将Base64编码的字符串解码为byte数组
        byte[] by = Base64.getDecoder().decode(cachedBase64Str);

        System.out.println(Arrays.toString(by));
    }

    /**
     * 高并发测试自定义雪花算法 100 个线程生成 1000 个id
     * <p>
     * 花费总时间: 7277 ms
     * 每个任务的平均耗时: 0.007277 ms
     * 重复id数量：0
     */
    @Test
    void testMiniSnow() {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < TASK_COUNT; j++) {
                        long id = SequenceUtils.nextId();
                        long threadId = Thread.currentThread().getId();
                        System.out.println("Thread " + threadId + " generated ID: " + id);
                        if (set.contains(id)) {
                            repeatd++;
                        } else {
                            set.add(id);
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        double averageTimePerTask = (double) totalTime / (THREAD_COUNT * TASK_COUNT);

        System.out.println("花费总时间: " + totalTime + " ms");
        System.out.println("每个任务的平均耗时: " + averageTimePerTask + " ms");
        System.out.println("重复id数量：" + repeatd);

        executorService.shutdown();
    }

    /**
     * 高并发测试雪花算法 100 个线程生成 1000 个id
     * <p>
     * 花费总时间: 4288 ms
     * 每个任务的平均耗时: 0.004288 ms
     * 重复id数量：0
     */
    @Test
    void testSnow() {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < TASK_COUNT; j++) {
                        long id = IdUtil.getSnowflakeNextId();
                        long threadId = Thread.currentThread().getId();
                        System.out.println("Thread " + threadId + " generated ID: " + id);
                        // 在这里可以根据需要进行进一步的测试操作，比如记录ID，验证唯一性等
                        if (set.contains(id)) {
                            repeatd++;
                        } else {
                            set.add(id);
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        double averageTimePerTask = (double) totalTime / (THREAD_COUNT * TASK_COUNT);

        System.out.println("花费总时间: " + totalTime + " ms");
        System.out.println("每个任务的平均耗时: " + averageTimePerTask + " ms");
        System.out.println("重复id数量：" + repeatd);

        executorService.shutdown();
    }
}

