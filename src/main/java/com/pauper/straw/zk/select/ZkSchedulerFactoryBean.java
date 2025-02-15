package com.pauper.straw.zk.select;

import com.pauper.straw.zk.ZkClient;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.utils.CloseableUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class ZkSchedulerFactoryBean extends SchedulerFactoryBean {

    private LeaderLatch leaderLatch;

    private final String LEADER_PATH = "/leader"; //namespace

    public ZkSchedulerFactoryBean() throws Exception {
        this.setAutoStartup(false); //应用启动的时候不自动开启定时任务

        leaderLatch = new LeaderLatch(ZkClient.getClient(), LEADER_PATH);
        leaderLatch.addListener(new DemoLeaderLatchListener(this)); //当leader发生变化的时候，需要触发监听
        leaderLatch.start();
    }

    @Override
    protected void startScheduler(Scheduler scheduler, int startupDelay) throws SchedulerException {
        if (this.isAutoStartup()) {
            super.startScheduler(scheduler, startupDelay);
        }
    }

    /**
     * 释放资源
     * @throws SchedulerException
     */
    @Override
    public void destroy() throws SchedulerException {
        CloseableUtils.closeQuietly(leaderLatch);
        super.destroy();
    }


}
