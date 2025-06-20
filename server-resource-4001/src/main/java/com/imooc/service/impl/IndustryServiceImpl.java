package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.db.Industry;
import com.imooc.mapper.IndustryMapper;
import com.imooc.service.IndustryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
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

    @Override
    public List<String> getTopList() {
        List<Industry> list = lambdaQuery().eq(Industry::getFatherId, 0)
                .orderByAsc(Industry::getSort)
                .list();
        return list.stream().map(Industry::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getChildrenIndustryList(Long industryId) {
        List<Industry> list = lambdaQuery().select(Industry::getName)
                .eq(Industry::getFatherId, industryId).orderByAsc(Industry::getSort).list();

        return list.stream().map(Industry::getName).collect(Collectors.toList());
    }

}
