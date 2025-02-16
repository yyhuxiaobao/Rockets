package com.pauper.straw.mq.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Configuration
public class RabbitConfig {

    /*@Bean
    public SimpleMessageConverter converter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(Arrays.asList("com.pauper.straw.mq.rabbit.*", "java.util.*"));
        return converter;
    }*/

    @Bean
    public Queue helloQ() {
        return new Queue("hello",false, false, false );
    }

    @Bean("directQueue")
    public Queue objectQ() {
        return new Queue("object");
    }

    @Bean("directExchange")
    public DirectExchange testDirectExchange(){
        return new DirectExchange("TestDirectExchange", true, false );
    }

    @Bean
    public Binding bindingDirect(@Qualifier("directQueue")Queue queue, @Qualifier("directExchange") DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("DirectRouting");
    }

    @Bean("fanoutQueueA")
    public Queue fanoutQA() {
        return new Queue("fanoutQA");
    }

    @Bean("fanoutQueueB")
    public Queue fanoutQB() {
        return new Queue("fanoutQB");
    }

    @Bean("fanoutExchange")
    public FanoutExchange testFanoutExchange(){
        return new FanoutExchange("TestFanoutExchange", true, false );
    }

    @Bean
    public Binding bindingFanoutA(@Qualifier("fanoutQueueA")Queue queue, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    @Bean
    public Binding bindingFanoutB(@Qualifier("fanoutQueueB")Queue queue, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    /*@Bean
    public Queue queue_one(){
        return new Queue("topic.one");
    }

    @Bean
    public Queue queue_two(){
        return new Queue("topic.two");
    }

    @Bean
    TopicExchange exchange(){
        return new TopicExchange("topicExchange");
    }

    @Bean
    Binding bindingExchangeOne(Queue queue_one, TopicExchange exchange){
        return BindingBuilder.bind(queue_one).to(exchange).with("topic.one");
    }

    @Bean
    Binding bindingExchangeTwo(Queue queue_two, TopicExchange exchange){
        //# 表示零个或多个词
        //* 表示一个词
        return BindingBuilder.bind(queue_two).to(exchange).with("topic.#");
    }*/


}
