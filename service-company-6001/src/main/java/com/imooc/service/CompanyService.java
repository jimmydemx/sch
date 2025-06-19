package com.imooc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.db.Company;

import java.util.List;

public interface CompanyService extends IService<Company> {

    public Company getCompanyByFullName(String fullName);


    public void addCompanies(List<Company> companies);
}
