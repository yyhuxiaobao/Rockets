package com.pauper.straw.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.Random;

public class InterProcessMutexRunnable implements Runnable{

    private CuratorFramework curatorFramework;

    public InterProcessMutexRunnable(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    @Override
    public void run() {
        try {
            // 模拟随机加入的分布式节点
            int randomSleep = new Random().nextInt(1000);
            Thread.sleep(randomSleep);
            String path = "/rootNode/lockNode";
            // 创建InterProcessMutex实例，用于获取分布式锁
            InterProcessMutex mutex = new InterProcessMutex(curatorFramework, path);
            // 阻塞，直到获取分布式锁
            mutex.acquire();
            if(mutex.isOwnedByCurrentThread()) {
                System.out.println(Thread.currentThread().getName() + " 持有分布式锁");
                mutex.getParticipantNodes().forEach(System.out::println);
                // 处理业务
                Thread.sleep(5000);
                // 业务处理完成
                System.out.println(Thread.currentThread().getName() + " 业务处理完成");
                // 释放分布式锁
                mutex.release();
            }
            else {
                throw new RuntimeException("获取分布式锁时被中断");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
