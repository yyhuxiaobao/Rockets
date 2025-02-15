package com.pauper.straw.zk.select;

import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class DemoLeaderLatchListener implements LeaderLatchListener {
    //控制定时任务启动和停止的方法
    private SchedulerFactoryBean schedulerFactoryBean;

    public DemoLeaderLatchListener(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @Override
    public void isLeader() {
        System.out.println(Thread.currentThread().getName()+"成为了leader");
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.start();
    }

    @Override
    public void notLeader() {
        System.out.println(Thread.currentThread().getName()+"抢占leader失败，不执行任务");
        schedulerFactoryBean.setAutoStartup(false);
        schedulerFactoryBean.stop();
    }

}
