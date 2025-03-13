package com.imooc.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class MobileValidation {

    private static final PhoneNumberUtil phoneUtil =  PhoneNumberUtil.getInstance();

    public static boolean isPhoneValidated(String mobile, String region){
        try{
            Phonenumber.PhoneNumber number = phoneUtil.parse(mobile, region);
            return phoneUtil.isValidNumber(number);
        }catch (NumberParseException e){
        return false;
        }
    }
}
