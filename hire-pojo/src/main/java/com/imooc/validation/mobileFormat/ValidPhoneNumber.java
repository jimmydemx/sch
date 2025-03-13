package com.imooc.validation.mobileFormat;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface ValidPhoneNumber {

    String message() default "手机号格式不正确"; // 默认错误信息

    /*
        如果不使用 groups，那么 @ValidPhoneNumber 注解会在所有 Bean 校验中都生效。
     */
    Class<?>[] groups() default {};  // 重点参数
    Class<? extends Payload>[] payload() default {};
}
