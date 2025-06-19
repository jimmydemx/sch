package com.imooc.controller;


import com.imooc.db.Company;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
@Slf4j
@Tag(name = "company", description = "company endpoint")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    @Operation(summary = "full name", description = "get by full name")
    public GraceJSONResult getByFullName(String fullName) {
        Company companyByFullName = companyService.getCompanyByFullName(fullName);
        if (companyByFullName == null) {
            return GraceJSONResult.exception(ResponseStatusEnum.COMPANY_NOT_EXIST);
        }

        return GraceJSONResult.OK(companyByFullName);
    }
}
