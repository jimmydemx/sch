package com.imooc.mq;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 构建简单模式的消费者，
 */
public class FooConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂以及相关的参数配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        // 通过工程创建Connection
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

//        channel.queueDeclare("foo", false, false, false, null);

        /**
         * queue: 监听的队列名
         * autoAck: 是否自动确认，true：告知mq消费者已经消费的确认通知
         * callback：回调很熟，吃力监听到的消息
         */

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            /**
             *
             * @param consumerTag 消息的标签（标识）
             * @param envelope 信封(一些信息，比如交换机路由等信息）
             * @param properties 配置信息
             * @param body 收到的消息数据
             * @throws IOException
             */
            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                System.out.println(new String(body));
                System.out.println("consumerTag = " + consumerTag);
                System.out.println("envelope = " + envelope);
                System.out.println("properties = " + properties);
            }
        };
        channel.basicConsume("foo",true,consumer);

    }
}
