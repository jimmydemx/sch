package com.imooc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.db.Industry;

public interface IndustryService extends IService<Industry> {

    public boolean getIndustryIsExistByName(String name);

    public Industry createIndustry(Industry industry);

}
