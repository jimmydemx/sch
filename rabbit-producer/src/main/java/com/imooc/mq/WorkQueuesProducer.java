package com.imooc.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 构建工作队列，一个队列会被多个消费者消费，没有配置的话，不同的Consumer将随机得到相关的生产。可能有负载均衡的考虑。
 */
public class WorkQueuesProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建config
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("work_queue", true, false, false, null);

        for (int i = 0; i < 10; i++) {
            String message = "Hello World! Work queue: " + i;
            channel.basicPublish("", "work_queue", null, message.getBytes());
        }
        channel.close();
        connection.close();
    }
}
