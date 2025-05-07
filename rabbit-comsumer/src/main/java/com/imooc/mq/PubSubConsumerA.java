package com.imooc.mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PubSubConsumerA {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        // 通过工程创建Connection
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String fanout_queue_a = "fanout_queue_a";
        channel.queueDeclare(fanout_queue_a, true, false, false, null);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            /**
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
            }
        };
        channel.basicConsume(fanout_queue_a,true,consumer);

    }


}
