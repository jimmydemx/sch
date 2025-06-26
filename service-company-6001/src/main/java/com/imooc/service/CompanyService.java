package com.imooc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.db.Company;
import com.imooc.db.ProvinceCompanyUserDTO;

import java.util.List;

public interface CompanyService extends IService<Company> {

    public Company getCompanyByFullName(String fullName);


    public void addCompanies(List<Company> companies);


    // task1 :  使用MyBatis注解joint 在 同一个province 的Company_name以及user_name的交叉列表
    public List<ProvinceCompanyUserDTO> getCompanyUserByProvince(String province);
    // task2 : 使用xml

    // task3 :
    // task4 :
    // task5 :
    // task6 :

}
