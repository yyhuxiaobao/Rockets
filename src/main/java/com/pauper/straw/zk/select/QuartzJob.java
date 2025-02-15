package com.pauper.straw.zk.select;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuartzJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("开始执行定时任务");
        System.out.println("当前执行的系统时间：" + sdf.format(new Date()));
        try {
            Thread.sleep(3*1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("执行定时任务结束");
        System.out.println("当前执行的系统时间：" + sdf.format(new Date()));
    }
}
