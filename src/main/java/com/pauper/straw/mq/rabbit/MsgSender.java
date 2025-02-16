package com.pauper.straw.mq.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Component
public class MsgSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendHelloQ() {
        String context = "hello " + new Date();
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }

    public void sendObjectQ() {
        UserO userO = new UserO();
        userO.setAge(20);
        userO.setName("测试");
        AdderO adderO = new AdderO();
        adderO.setCity("杭州");
        adderO.setLocal("上城区某街道");
        adderO.setProv("浙江省");
        userO.setAdderO(adderO);
        this.rabbitTemplate.convertAndSend("TestDirectExchange","DirectRouting", userO);
    }

    public void sendFanoutQ() {
        UserO userO = new UserO();
        userO.setAge(20);
        userO.setName("测试");
        AdderO adderO = new AdderO();
        adderO.setCity("杭州");
        adderO.setLocal("上城区某街道");
        adderO.setProv("浙江省");
        userO.setAdderO(adderO);
        this.rabbitTemplate.convertAndSend("TestFanoutExchange", null,userO);
    }

    //两个消息接受者都可以收到
    @Transactional(rollbackFor = Exception.class)
    public void send_one() {
        String context = "Hi, I am message one";
        System.out.println("Sender : " + context);
        log.info("开启事务消息机制");
        this.rabbitTemplate.convertAndSend("topicExchange","topic.one",context);
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //只有TopicReceiverTwo都可以收到
    public void send_two() {
        String context = "Hi, I am message two";
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("topicExchange","topic.two",context);
    }

}
