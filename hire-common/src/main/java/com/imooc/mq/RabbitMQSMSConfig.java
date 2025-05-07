package com.imooc.mq;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * RabbitMQ的配置类
 * 1. 创建交换机
 * 2，创建队列
 * 3，创建绑定关系
 */
@Configuration
public class RabbitMQSMSConfig {

    public static final String SMS_EXCHANGE = "sms_exchange";
    public static final String SMS_ROUTING_KEY = "imooc.sms.#";
    public static final String SMS_QUEUE = "sms_queue";
    public static final Integer TTL = 30*1000;

    @Bean(SMS_EXCHANGE)
    public Exchange exchange(){
        return ExchangeBuilder.topicExchange(SMS_EXCHANGE).durable(true).build();
    }

    @Bean(SMS_QUEUE)
    public Queue queue(){
        return QueueBuilder.durable(SMS_QUEUE)
                .withArgument("x-message-ttl", TTL)  // 设置所有消息在这个队列中能够live的时间是10s，超过以后此消息就会删除
                .withArgument("x-dead-letter-exchange", RabbitMQSMSConfig_Dead.SMS_EXCHANGE_DEAD)
                .withArgument("x-dead-letter-routing-key", "dead.sms.mq")
                .withArgument("x-max-length", 6)
                .build();
    }

    @Bean
    public Binding smsBinding(@Qualifier(SMS_EXCHANGE) Exchange exchange, @Qualifier(SMS_QUEUE) Queue queue ){
        return BindingBuilder.bind(queue).to(exchange).with(SMS_ROUTING_KEY).noargs();
    }

}
