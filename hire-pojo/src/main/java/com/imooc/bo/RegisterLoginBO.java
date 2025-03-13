package com.imooc.bo;

import com.imooc.validation.mobileFormat.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterLoginBO {

    @NotBlank(message = "手机号不能为空")
    @ValidPhoneNumber
    private String mobile;

    @NotBlank(message = "验证码不能为空")
    private String smsCode;
}
