package com.pauper.straw.mq.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component

public class MsgReceiver {
    @RabbitHandler
    @RabbitListener(queues = "hello")
    public void processHelloQ(String msg) {
        System.out.println("Receiver  : " + msg);
    }

    @RabbitHandler
    @RabbitListener(queues = "object")
    public void processObjectQ(UserO msg) {
        System.out.println("Receiver  ObjectQ: " + msg.getAdderO().getCity());
    }

    @RabbitHandler
    @RabbitListener(queues = "fanoutQA")
    public void processFanoutQA(UserO msg) {
        System.out.println("Receiver  fanoutQA: " + msg.getAdderO().getCity());
    }

    @RabbitHandler
    @RabbitListener(queues = "fanoutQB")
    public void processFanoutQB(UserO msg) {
        System.out.println("Receiver  fanoutQB: " + msg.getAdderO().getCity());
    }

    /*@RabbitHandler
    @RabbitListener(queues = "topic.one")
    public void processTopicOne(String msg) {
        System.out.println("Receiver  TopicOne: " + msg);
    }*/

    @RabbitHandler
    @RabbitListener(queues = "topic.two")
    public void processTopicTwo(String msg) {
        System.out.println("Receiver  TopicTwo: " + msg);
    }
}
