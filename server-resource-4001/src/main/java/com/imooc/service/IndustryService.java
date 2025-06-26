package com.imooc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.db.Industry;

import java.util.List;

public interface IndustryService extends IService<Industry> {

    public boolean getIndustryIsExistByName(String name);

    public Industry createIndustry(Industry industry);

    public List<String> getTopList();

    public List<String> getChildrenIndustryList(Long industryId);

    public void deleteIndustry(Long industryId);

    public List<String> getThirdListByTop(Long topIndustryId);

    public List<String> getGrandChildrenIndustryList(Industry industry);

    public boolean updateIndustryName(String name, Long industryId, Integer level);

}
