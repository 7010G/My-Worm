package cn.yumben.test.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zzg
 */
public class TheThreadPool {

    private static ThreadPoolExecutor threadPool = null;
    /**
     * 线程池核心池的大小
     */
    public static int CORE_POOL_SIZE = 80;
    /**
     * 线程池最大线程数
     */
    public static int MAX_IMUM_POOL_SIZE = 80;
    /**
     * 当线程数大于核心时此为终止前多余的空闲线程等待新任务的最长时间
     */
    public static int KEEP_ALIVE_TIME = 3;

    /**
     * 用来储存等待执行任务的队列大小
     */
    public static int CAPACITY = 400;

    //  int corePoolSize, - 线程池核心池的大小。
    //  int maximumPoolSize, - 线程池的最大线程数。
    //  long keepAliveTime, - 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
    //  TimeUnit unit, - keepAliveTime 的时间单位。
    //  BlockingQueue<Runnable> workQueue, - 用来储存等待执行任务的队列。
    //  ThreadFactory threadFactory, - 线程工厂。

    public static ThreadPoolExecutor getThreadPool() {


        synchronized (ExecutorService.class) {
            if (threadPool == null) {
                //构造一个线程池
                threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_IMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<Runnable>(CAPACITY),
                        new ThreadPoolExecutor.DiscardOldestPolicy());
            }
        }
        return threadPool;
    }

}
