package com.imooc.controller;

import com.imooc.bo.RegisterLoginBO;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.mq.RabbitMQSMSConfig;
import com.imooc.mq.SMSContentQO;
import com.imooc.retry.RetryComponent;
import com.imooc.utils.GsonUtils;
import com.imooc.utils.IpUtils;
import com.imooc.utils.MobileValidation;
import com.imooc.utils.RedisOperators;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/passport")
@Tag(name = "passport",description = "passport control")
public class PassportController {

    @Autowired
    private RedisOperators redisOperators;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/hello")
    @Operation(summary = "hello",description = "test api")
    public GraceJSONResult Hello(){
        return GraceJSONResult.OK("hello world");
    }

    @GetMapping("/helloRedis")
    @Operation(summary = "helloRedis",description = "test Redis")
    public GraceJSONResult HelloRedis(){
        redisOperators.setString("hello","word",1000);
        return GraceJSONResult.OK("hello redis");
    }


    @PostMapping("getsmscode")
    @Operation(summary ="SMS CODE",description = "get SMS code")
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request){
        // 判断是否是正确的module号码
       if(!MobileValidation.isPhoneValidated(mobile)){
           return GraceJSONResult.errorCustom(ResponseStatusEnum.MOBILE_FORMAT_ERROR);
       }
        // 获得用户IP，限制用户只能60s
        String requestIp = IpUtils.getRequestIp(request);
        redisOperators.setnx60s("MOBILE_SMSCODE_"+requestIp,mobile);
        // 模拟生成随机的验证码
        String code = (int)((Math.random() * 9 + 1) * 100000) + "";
        log.info("验证码为: {}",code);
        // 生成以后，存入redis，等待过期
        redisOperators.setString("MOBILE_SMSCODE_"+mobile,code,1800);

        // 使用消息队列衣服解耦发短信
        SMSContentQO qo = new SMSContentQO();
        qo.setMobile(mobile);
        qo.setContent(code);

        // 定义confirm回调
         rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData: 在convertAndSend中可以加的一个参数
             * @param ack：交换机是否成功接收到消息
             * @param cause：失败，失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info(correlationData.toString());
                if (ack) {
                    log.info("交换机成功接收到消息", cause);
                } else {
                    log.info("交换机接受消息失败", cause);
                }
            }
        });

        /**
         * 定义return回调
         *     private final Message message;
         *     private final int replyCode;
         *     private final String replyText;
         *     private final String exchange;
         *     private final String routingKey;
         *     information example:
         *      ReturnedMessage [message=(Body:'{"mobile":"+8618612392831","content":"113666"}'
         *     MessageProperties [headers={spring_returned_message_correlation=74974e92-ac44-4fa6-aa30-c76599cf3988},
         *     contentType=text/plain, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, deliveryTag=0]),
         *     replyCode=312, replyText=NO_ROUTE, exchange=sms_exchange, routingKey=FAWFWEimooc.sms.send.login]
         */

        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
             @Override
             public void returnedMessage(ReturnedMessage returnedMessage) {
                 log.info("进入rabbitMQ return");
                 log.info(returnedMessage.toString());
             }
         });

        // 消息属性处理的类对象，对当前需要的超时ttl进行参数属性的设置
        // 这个是对发送的的具体消息进行一个ttl事件控制，如果对整个队列需要在config中对于QUEUE进行设置，.withArgument("x-message-ttl", TTL)
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {

            @Override
            public Message postProcessMessage(Message message) {
                message.getMessageProperties().setExpiration(String.valueOf(10*1000));
                return message;
            }
        };
        rabbitTemplate.convertAndSend(RabbitMQSMSConfig.SMS_EXCHANGE,"imooc.sms.send.login",
                GsonUtils.object2String(qo),messagePostProcessor,
                new CorrelationData(UUID.randomUUID().toString()));

//        rabbitTemplate.convertAndSend(RabbitMQSMSConfig.SMS_EXCHANGE,"imooc.sms.send.login",
//                GsonUtils.object2String(qo),
//                new CorrelationData(UUID.randomUUID().toString()));
        return GraceJSONResult.OK();
    }

    @PostMapping("/login")
    @Operation(summary = "LOG IN",description = "LOG IN")
    public GraceJSONResult login(@Valid @RequestBody RegisterLoginBO registerLoginBO, HttpServletRequest request){

        // 在redis中寻找，如果找不到，返回错误
        String key = "MOBILE_SMSCODE_"+registerLoginBO.getMobile();
        String code = registerLoginBO.getSmsCode();
        String redisCode = redisOperators.getValue(key);

        if(StringUtils.isBlank(redisCode)|| !redisCode.equalsIgnoreCase(code)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 在数据库中查找，是否有这个手机号，


        // 如果数据为空，必须要信息入库

        // 范围jwttoken

        // 删除redis短信验证码

        // 返回userVO


        return GraceJSONResult.OK();
    }



}
