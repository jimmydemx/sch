package com.imooc.controller.mq;


import com.imooc.mq.RabbitMQSMSConfig;
import com.imooc.mq.RabbitMQSMSConfig_Dead;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class RabbitMQSMSConsumer_Dead {
    /**
     * 监听队列,并且处理消息，如果是在配置中 不设置 acknowledge-mode（auto），可以使用下面
     * @param msg
     * @param message
     */
//    @RabbitListener(queues ={RabbitMQSMSConfig.SMS_QUEUE})
//    public void receiveWithAutoACK(String msg, Message message) {
//        log.info("receive with Auto ACK.....");
//        log.info("receive message: {}", msg);
//        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
//        log.info("receivedRoutingKey: {}", receivedRoutingKey);
//    }

    @RabbitListener(queues = {RabbitMQSMSConfig_Dead.SMS_QUEUE_DEAD})
    public void receiveWithManualACK(String msg, Message message, Channel channel) throws IOException {
        log.info("receive with Manual ACK.....");
        log.info("receive message: {}", msg);
        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("receivedRoutingKey: {}", receivedRoutingKey);



        /**
         * 参数：
         * deliveryTag: 消息投递的标签
         * multiple:批量手动确认消费者获得的信息
         */
        try{
            // test code for errors
//            int i=1/0;
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        }catch (Exception e){
            e.printStackTrace();
            /**
             * 参数；同上
             * 最后一个参数：true 重回队列 false：丢弃消息，但是如果一直报错就会一直重回队列
             */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);
        }

    }
}
