package com.imooc.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *  构建简单模式的生产者
 *  - 配置参数
 *  - 创建连接
 *  - 创建管道
 *  - 创建队列（简单模式不需要交换机exchange)
 *  - 像队列发送消息
 */
public class FooProducer {

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

        /**
         * queue: 队列名
         * durable：是否持久化，true: 重启以后，队列依然存在，false则不存在
         * exclusive：是否独占(只能有一个消费者监听这个队列), 一般设置为false
         * autoDelete：是否自动删除，true 当没有消费者的时候，自动删除这个队列
         * arguments： map类型其它参数
         */
        channel.queueDeclare("foo", true, false, false, null);

        /**
         * 发送消息
         */
        String message = "Hello World!";
        channel.basicPublish("", "foo", null, message.getBytes());
        channel.close();
        connection.close();

    }
}
