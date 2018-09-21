package com.woqutz.didi.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池
 * Created by anshu.wang on 2017/3/29.
 */

public class ThreadPool {
    private static ThreadPool instance;
    private final ExecutorService executorService;


    public static ThreadPool getInstance() {
        if (instance == null) {
            synchronized (ThreadPool.class) {
                if (instance == null) {
                    System.out.println("===>创建了的对象");
                    instance = new ThreadPool();
                }
            }

        }
        return instance;
    }

    private ThreadPool() {
        int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        int CORE_POOL_SIZE = CPU_COUNT + 1;
        int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
        int KEEP_ALIVE = 1;
        executorService = Executors.newFixedThreadPool(CPU_COUNT > 0 ? CPU_COUNT * 2 : 5, sThreadFactory);
    }

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "Didi Thread #" + mCount.getAndIncrement());
        }
    };

    public void addThread(Runnable runnable) {
        executorService.execute(runnable);
    }

    public static void main(String[] args) {
        ThreadPool.getInstance().addThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("a");
            }
        });
        ThreadPool.getInstance().addThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("n");
            }
        });
    }
}
