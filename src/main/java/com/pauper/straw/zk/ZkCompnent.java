package com.pauper.straw.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ZkCompnent {

    @Value("${zk.curator.baseSleepTimeMs}")
    private int baseSleepTimeMs;
    @Value("${zk.curator.maxRetries}")
    private int maxRetries;
    @Value("${zk.curator.connectStr}")
    private String connectStr;
    @Value("${zk.curator.sessionTimeoutMs}")
    private int sessionTimeoutMs;
    @Value("${zk.curator.connectionTimeoutMs}")
    private int connectionTimeoutMs;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework(){
        //重试机制, 每个1秒钟重试1次，最多重试5次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(connectStr,
                sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        // 建立连接
        return curatorFramework;
    }

}
