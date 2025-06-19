package com.imooc.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.db.Company;
import com.imooc.exceptions.MyCustomException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.mapper.CompanyMapper;
import com.imooc.service.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComapnyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
    @Override
    public Company getCompanyByFullName(String fullName) {
        return lambdaQuery().eq(Company::getCompany_name, fullName).one();
    }

    @Override
    @Transactional
    public void addCompanies(List<Company> companies) {
        boolean b = saveBatch(companies);
        if (!b) {
            throw new MyCustomException(ResponseStatusEnum.FAILED);
        }
    }

}
