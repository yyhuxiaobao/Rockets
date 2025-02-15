package com.pauper.straw.mq.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "hello")
public class MsgReceiver {
    @RabbitHandler
    public void process(String msg) {
        System.out.println("Receiver  : " + msg);
    }
}
