package com.imooc.mq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * RabbitMQ的配置类，用于死信队列，有3种情况
 * 1，ttl时间超时丢弃
 * 2，队列长度超过queue的大小
 * 3，
 */
@Configuration
public class RabbitMQSMSConfig_Dead {

    public static final String SMS_EXCHANGE_DEAD = "sms_exchange_dead";
    public static final String SMS_ROUTING_KEY_DEAD = "dead.sms.#";
    public static final String SMS_QUEUE_DEAD = "sms_queue_dead";
    public static final Integer TTL = 10*1000;

    @Bean(SMS_EXCHANGE_DEAD)
    public Exchange exchange(){
        return ExchangeBuilder.topicExchange(SMS_EXCHANGE_DEAD).durable(true).build();
    }

    @Bean(SMS_QUEUE_DEAD)
    public Queue queue(){
        return QueueBuilder.durable(SMS_QUEUE_DEAD)
                .build();
    }

    @Bean
    public Binding smsDeadBinding(@Qualifier(SMS_EXCHANGE_DEAD) Exchange exchange, @Qualifier(SMS_QUEUE_DEAD) Queue queue ){
        return BindingBuilder.bind(queue).to(exchange).with(SMS_ROUTING_KEY_DEAD).noargs();
    }

}
