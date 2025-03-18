package com.pauper.straw.zk;

import lombok.Data;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class ZookeeperCuratorClient implements InitializingBean {

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

    private CuratorFramework curatorFramework;

    @PostConstruct
    public void init(){
        System.out.println("init");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
        //重试机制, 每个1秒钟重试1次，最多重试5次
        /*RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        curatorFramework = CuratorFrameworkFactory.newClient(connectStr,
                sessionTimeoutMs, connectionTimeoutMs, retryPolicy);*/
    }

    public CuratorFramework getCuratorClient() {
        return curatorFramework;
    }
}
