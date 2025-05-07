package com.imooc.mq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 构建通配符模式的producer
 */
public class TopicProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String topic_exchange = "topic_exchange";
        channel.exchangeDeclare(topic_exchange, BuiltinExchangeType.TOPIC,true,false,false,null);

        String topic_queue_order = "topic_queue_order";
        String topic_queue_pay = "topic_queue_pay";

        channel.queueDeclare(topic_queue_order,true,false,false,null);
        channel.queueDeclare(topic_queue_pay,true,false,false,null);

        channel.queueBind(topic_queue_order,topic_exchange,"order.*");
        channel.queueBind(topic_queue_pay,topic_exchange,"*.pay.#");


        // publish below
        String msg1 = "创建订单A";
        String msg2 = "创建订单B";
        String msg3 = "删除订单C";
        String msg4 = "修改订单B";
        String msg5 = "支付订单E";
        String msg6 = "超市支付订单F";

        channel.basicPublish(topic_exchange,"order.create",null,msg1.getBytes());
        channel.basicPublish(topic_exchange,"a.order.create",null,msg2.getBytes());
        channel.basicPublish(topic_exchange,"order.delete",null,msg3.getBytes());
        channel.basicPublish(topic_exchange,"order.update",null,msg4.getBytes());
        channel.basicPublish(topic_exchange,"order.pay",null,msg5.getBytes());
        channel.basicPublish(topic_exchange,"a.pay.supermarket",null,msg6.getBytes());


        channel.close();
        connection.close();

    }


}
