package com.imooc.mq;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 构建发布订阅的生产者
 */
public class PubSubProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        // 创建交换机 Exchange
        /**
         *  exchange: 交换机名称
         *  type: 交换机类型
         *  FANOUT("fanout") 广播模式，发布订阅，把消息发送给所有绑定的队列
         *  DIRECT("direct")定向投递模式，把消息发送给指定"routing pattern"的队列
         *  TOPIC("topic") 通配符模式，把消息发送给符合"routing pattern"的队列
         *  HEADER(“headers”): 使用频率不多，参数匹配
         *  durable: 是否持久化
         *  autoDelete:是否自动删除
         *  internal：true 表示当前exchange是rabbitmq内部使用， 用户创建的队列不会消费该类型交换机下的消息，所以一般会使用false
         *  arguments: map类型的参数
         */
        String exchangeName = "exchange";

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT,true,false,false,null);

        String fanout_queue_a="fanout_queue_a";
        String fanout_queue_b="fanout_queue_b";
        channel.queueDeclare(fanout_queue_a,true,false,false,null);
        channel.queueDeclare(fanout_queue_b,true,false,false,null);

        // 绑定交换机和队列
        channel.queueBind(fanout_queue_a,exchangeName,"");
        channel.queueBind(fanout_queue_b,exchangeName,"");

        for (int i = 0; i < 10; i++) {
            String  task = "开始任务有2个queue，已经使用exchange:"+i;
            channel.basicPublish(exchangeName,"",null,task.getBytes());
        }

        channel.close();
        connection.close();

    }


}
