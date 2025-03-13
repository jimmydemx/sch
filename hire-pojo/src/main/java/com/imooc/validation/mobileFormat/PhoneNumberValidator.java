package com.imooc.validation.mobileFormat;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber,String> {

    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {

            Phonenumber.PhoneNumber zz = phoneUtil.parse(s, "ZZ");
            if(phoneUtil.isValidNumber(zz)){
                return true;
            }
        } catch (NumberParseException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
