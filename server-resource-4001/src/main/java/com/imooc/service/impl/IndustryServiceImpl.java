package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.aspects.redisCache.RedisCache;
import com.imooc.db.Industry;
import com.imooc.mapper.IndustryMapper;
import com.imooc.service.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndustryServiceImpl extends ServiceImpl<IndustryMapper, Industry> implements IndustryService {


    @Autowired
    private IndustryMapper industryMapper;

    @Override
    public boolean getIndustryIsExistByName(String name) {
        Industry industry = lambdaQuery().eq(Industry::getName, name).one();
        return industry != null;
    }

    @Override
    @Transactional
    public Industry createIndustry(Industry industry) {
        saveOrUpdate(industry);

        return industry;
    }

    @Override
    @RedisCache(action = "search", key = "'T(com.imooc.aspects.redisCache.RedisIndustryKeyConstructUtil).getIndustryKey(#industryId,1)'")
    public List<String> getTopList() {
        List<Industry> list = lambdaQuery().eq(Industry::getFatherId, 0)
                .orderByAsc(Industry::getSort)
                .list();
        return list.stream().map(Industry::getName).collect(Collectors.toList());
    }

    @Override
    @RedisCache(action = "search", key = "'T(com.imooc.aspects.redisCache.RedisIndustryKeyConstructUtil).getIndustryKey(#industryId,1)'")
    public List<String> getChildrenIndustryList(Long industryId) {
        List<Industry> list = lambdaQuery().select(Industry::getName)
                .eq(Industry::getFatherId, industryId).orderByAsc(Industry::getSort).list();

        return list.stream().map(Industry::getName).collect(Collectors.toList());
    }


    // 需要使用 RedisIndustryKeyConstructUtil进行优化
    @Override
    @RedisCache(action = "search", key = "T(com.imooc.aspects.redisCache.RedisIndustryKeyConstructUtil).getIndustryKey(#industry.id, 3)")
    public List<String> getGrandChildrenIndustryList(Industry industry) {
        List<String> grandChildrenIndustryList = industryMapper.getGrandChildrenIndustryList(industry.getId());
        return grandChildrenIndustryList;
    }

    // need to know the level
    @Override
    @Transactional
    @RedisCache(action = "delete", key = "T(com.imooc.aspects.redisCache.RedisIndustryKeyConstructUtil).getIndustryKey(#industryId,#level)")
    public boolean updateIndustryName(String name, Long industryId, Integer level) {
        return lambdaUpdate().eq(Industry::getId, industryId).set(Industry::getName, name).update();
    }


    @Override
    @Transactional
    @RedisCache(action = "delete", key = "T(com.imooc.aspects.redisCache.RedisIndustryKeyConstructUtil).getIndustryKey(#industryId)")
    public void deleteIndustry(Long industryId) {
        lambdaUpdate()
                .eq(Industry::getId, industryId)
                .notExists(
                        "(SELECT 1 FROM (SELECT 1 FROM industry WHERE father_id = {0} LIMIT 1) tmp)", industryId)
                .remove();
    }


    @Override
    public List<String> getThirdListByTop(Long topIndustryId) {

        // 先从redis查询，如果没有没有，然后再在那个db中，并且存储

        List<Industry> list = lambdaQuery().inSql(Industry::getFatherId, "SELECT id FROM industry WHERE father_id = " + topIndustryId)
                .list();
        return list.stream().map(Industry::getName).collect(Collectors.toList());
    }


//    private void resetRedisIndustry(Industry industry) {
//        if (industry.getLevel() == 1) {
//                redis
//        }
//
//    }
}
