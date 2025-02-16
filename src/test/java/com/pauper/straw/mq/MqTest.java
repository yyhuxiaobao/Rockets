package com.pauper.straw.mq;

import com.pauper.straw.mq.rabbit.MsgSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class MqTest {

    @Autowired
    private MsgSender msgSender;

    @Test
    public void testSenderHelloQ(){
        msgSender.sendHelloQ();
    }

    @Test
    public void testSenderObjectQ(){
        msgSender.sendObjectQ();
    }

    @Test
    public void testSenderFanoutQ(){
        msgSender.sendFanoutQ();
    }

    @Test
    public void testSenderTopicQ(){
        msgSender.send_one();
        //msgSender.send_two();
    }

}
