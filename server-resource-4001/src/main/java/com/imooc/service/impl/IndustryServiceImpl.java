package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.db.Industry;
import com.imooc.mapper.IndustryMapper;
import com.imooc.service.IndustryService;
import org.springframework.transaction.annotation.Transactional;

public class IndustryServiceImpl extends ServiceImpl<IndustryMapper, Industry> implements IndustryService {
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
}
