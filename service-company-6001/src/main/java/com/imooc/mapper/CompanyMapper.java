package com.imooc.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.db.Company;
import com.imooc.db.ProvinceCompanyUserDTO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyMapper extends BaseMapper<Company> {

    @Select("""
            SELECT c.company_name, c.province,u.nickname FROM company c
            left join users u on c.province=u.province
            Where c.province=#{province}                                            
            order by c.company_name
            """)
    List<ProvinceCompanyUserDTO> getCompanyUserByProvice(String province);
}
