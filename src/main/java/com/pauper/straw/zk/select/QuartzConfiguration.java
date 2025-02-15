package com.pauper.straw.zk.select;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {

    //触发
    @Bean
    public ZkSchedulerFactoryBean schedulerFactoryBean(JobDetail jobDetail, Trigger trigger) throws Exception {
        ZkSchedulerFactoryBean zkSchedulerFactoryBean = new ZkSchedulerFactoryBean();
        zkSchedulerFactoryBean.setJobDetails(jobDetail);
        zkSchedulerFactoryBean.setTriggers(trigger);
        return zkSchedulerFactoryBean;
    }

    //定时任务
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(QuartzJob.class).storeDurably().build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        //定义一个简单执行器，一秒执行一次，重复执行。
        SimpleScheduleBuilder simpleScheduleBuilder =
                SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever();
        return TriggerBuilder.newTrigger().forJob(jobDetail).withSchedule(simpleScheduleBuilder).build();
    }

}
