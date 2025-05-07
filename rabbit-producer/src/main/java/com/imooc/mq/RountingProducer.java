package com.imooc.mq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *  使用路由定向发送，也需要使用exchange
 */
public class RountingProducer {
    public static void main(String[] args) throws Exception {
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
        String routing_exchange = "routing_exchange";

        channel.exchangeDeclare(routing_exchange, BuiltinExchangeType.DIRECT,true,false,false,null);

        String routing_queue_order="routing_queue_order";
        String routing_queue_pay="routing_queue_pay";
        channel.queueDeclare(routing_queue_order,true,false,false,null);
        channel.queueDeclare(routing_queue_pay,true,false,false,null);

        // 绑定交换机和队列
        channel.queueBind(routing_queue_order,routing_exchange,"order_create");
        channel.queueBind(routing_queue_order,routing_exchange,"order_delete");
        channel.queueBind(routing_queue_order,routing_exchange,"order_update");
        channel.queueBind(routing_queue_pay,routing_exchange,"order_pay");


        String msg1 = "创建订单A";
        String msg2 = "创建订单B";
        String msg3 = "删除订单C";
        String msg4 = "修改订单B";
        String msg5 = "支付订单E";
        String msg6 = "支付订单F";
        channel.basicPublish(routing_exchange,"order_create",null,msg1.getBytes());
        channel.basicPublish(routing_exchange,"order_create",null,msg2.getBytes());
        channel.basicPublish(routing_exchange,"order_delete",null,msg3.getBytes());
        channel.basicPublish(routing_exchange,"order_update",null,msg4.getBytes());
        channel.basicPublish(routing_exchange,"order_pay",null,msg5.getBytes());
        channel.basicPublish(routing_exchange,"order_pay",null,msg6.getBytes());

        channel.close();
        connection.close();

    }
}
