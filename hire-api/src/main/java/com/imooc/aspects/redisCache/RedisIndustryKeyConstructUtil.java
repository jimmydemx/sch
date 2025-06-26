package com.imooc.aspects.redisCache;

import com.imooc.base.BaseInfoProperties;

public class RedisIndustryKeyConstructUtil extends BaseInfoProperties {


    public static String getIndustryKey(Long industryId, Integer level) {
//        Long id = industry.getId();
        String keyString = getKeyString(industryId, level);
        return keyString;

    }

//    public static String getIndustryKey(Industry industry) {
//        Integer level = industry.getLevel();
//        String keyString = getIndustryKey(industry, level);
//        return keyString;
//    }

    private static String getKeyString(Long id, Integer level) {
        switch (level) {
            case 1:
                return TOP_INDUSTRY_LIST + "::" + id;
            case 2:
                return SECOND_INDUSTRY_LIST + "::" + id;
            case 3:
                return THIRD_INDUSTRY_LIST + "::" + id;

        }
        return "";
    }

}
