package com.imooc.controller;

import com.google.gson.JsonObject;
import com.imooc.Users;
import com.imooc.bo.RegisterLoginBO;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.intercept.JWTCurrentUserInterceptor;
import com.imooc.mq.RabbitMQSMSConfig;
import com.imooc.mq.SMSContentQO;
import com.imooc.service.UsersService;
import com.imooc.utils.*;
import com.imooc.vo.UsersVO;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.imooc.base.BaseInfoProperties.APP_USER_JSON;
import static com.imooc.base.BaseInfoProperties.TOKEN_USER_PREFIX;

@RestController
@Slf4j
@RequestMapping("/passport")
@Tag(name = "passport", description = "passport control")
public class PassportController {

    @Autowired
    private RedisOperators redisOperators;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UsersService usersService;

    @Autowired
    private JWTUtils jwtUtils;

    @Value("${auth.mobile.smscode.prefix}")
    private String mobile_prefix;


    @GetMapping("/hello")
    @Operation(summary = "hello", description = "test api")
    public GraceJSONResult Hello(
            HttpServletRequest request
    ) {

        // 方法一：获得JWT中的相关信息
        String header = request.getHeader(APP_USER_JSON);
        if (!StringUtils.isBlank(header)) {
            JsonObject jsonObject = GsonUtils.string2Object(header);
            log.info("header:{}", jsonObject);

        }


        // 方法二： 获得jwt中相关的星系

        Users users = JWTCurrentUserInterceptor.currentUser.get();
        log.info("users:{}", users);

        return GraceJSONResult.OK("hello world");
    }

    @GetMapping("/helloRedis")
    @Operation(summary = "helloRedis", description = "test Redis")
    public GraceJSONResult HelloRedis() {
        redisOperators.setString("hello", "word", 1000);
        return GraceJSONResult.OK("hello redis");
    }

    /**
     * 1.验证手机号是否正确，不正确返回
     * 2，用户只能1分钟发一次请求，（用户使用ip，如果再来一次，就返回错误）
     * 3，然后，发起得到一个码，再把手机号以及这个保存，设置时间
     * 4，
     *
     * @param mobile
     * @param request
     * @return
     */

    @PostMapping("getsmscode")
    @Operation(summary = "SMS CODE", description = "get SMS code")
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) {
        // 判断是否是正确的module号码
        if (!MobileValidation.isPhoneValidated(mobile)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.MOBILE_FORMAT_ERROR);
        }
        // 获得用户IP，限制用户只能60s
        String requestIp = IpUtils.getRequestIp(request);
        redisOperators.setnx60s(mobile_prefix + requestIp, mobile);
        // 模拟生成随机的验证码
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        log.info("验证码为: {}", code);
        // 生成以后，存入redis，等待过期
        redisOperators.setString(mobile_prefix + mobile, code, 1800);

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
                message.getMessageProperties().setExpiration(String.valueOf(10 * 1000));
                return message;
            }
        };
        rabbitTemplate.convertAndSend(RabbitMQSMSConfig.SMS_EXCHANGE, "imooc.sms.send.login",
                GsonUtils.object2String(qo), messagePostProcessor,
                new CorrelationData(UUID.randomUUID().toString()));

//        rabbitTemplate.convertAndSend(RabbitMQSMSConfig.SMS_EXCHANGE,"imooc.sms.send.login",
//                GsonUtils.object2String(qo),
//                new CorrelationData(UUID.randomUUID().toString()));
        return GraceJSONResult.OK();
    }

    @PostMapping("/login")
    @Operation(summary = "LOG IN", description = "LOG IN")
    public GraceJSONResult login(@Valid @RequestBody RegisterLoginBO registerLoginBO, HttpServletRequest request) {

        // 在redis中寻找，如果找不到，返回错误
        String mobile = registerLoginBO.getMobile();
        String key = mobile_prefix + mobile;
        String code = registerLoginBO.getSmsCode();
        String redisCode = redisOperators.getValue(key);

        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(code)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 在数据库中查找，是否有这个手机号
        Users userByMobile = usersService.getUserByMobile(registerLoginBO.getMobile());
        if (userByMobile == null) {
            // 如果数据为空，必须要信息入库
            userByMobile = usersService.createUser(registerLoginBO.getMobile());
        }


        // 范围jwttoken
//        Users userForJWT = new Users();
//        userForJWT.setId(userByMobile.getId());
//        userForJWT.setMobile(userByMobile.getMobile());
//        userForJWT.setRole(userByMobile.getRole());
        String jwtWithPrefix = jwtUtils.createJWTWithPrefix(GsonUtils.object2String(userByMobile), Long.valueOf(6000 * 1000), TOKEN_USER_PREFIX);
        // 拿到jwt不需要进行放入redis吗？

        // 删除redis短信验证码
        redisOperators.deleteKey(key);

        // 返回用户信息给前端
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userByMobile, usersVO);
        usersVO.setUserToken(jwtWithPrefix);


        // 创建建立，使用open-feign创建建立模板

        // InitRequest initRequest = new InitRequest();
        //    initRequest.setUserId("12345");
        //    return workMicroServiceFeign.init(initRequest);
        //  int a=1/0;

        // important:  workMicroServiceFeign创建成功修改数据库后遇到了
        // a=1/0的异常情况，workMicroServiceFeign不会回滚。出现了数据不一致

        return GraceJSONResult.OK(usersVO);
    }

    @PostMapping("logout")
    @Operation(summary = "LOG out", description = "LOG out")
    private GraceJSONResult logout(@RequestParam String userId, HttpServletRequest request) {
        redisOperators.deleteKey(userId);
        return GraceJSONResult.OK();
    }


    @GetMapping
    @Operation(summary = "Get user by mobile", description = "Get user by mobile")
    public GraceJSONResult getUserByMobile(String mobile) {

        Users userByMobile = usersService.getUserByMobile(mobile);
        return GraceJSONResult.OK(userByMobile);
    }

}
