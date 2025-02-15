package com.pauper.straw.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZkClient {

    //重试之间等待的初始时间
    private static final int RETRY_BASE_SLEEP_TIME_MS = 1000;
    //最大重试次数
    private static final int RETRY_MAX_NUM = 5;

    //要连接的服务器列表
    private static final String CONNECT_STR = "localhost:2181";
    //session 超时时间
    private static final int SESSION_TIMEOUT_MS = 30000;
    //连接超时时间
    private static final int CONNECTION_TIMEOUT_MS = 60000;

    public static  CuratorFramework getClient(){
        //重试机制, 每个1秒钟重试1次，最多重试5次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(RETRY_BASE_SLEEP_TIME_MS, RETRY_MAX_NUM);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECT_STR,
                SESSION_TIMEOUT_MS, CONNECTION_TIMEOUT_MS, retryPolicy);
        // 建立连接
        curatorFramework.start();
        return curatorFramework;
    }

}
