package com.imooc.controller;


import com.imooc.db.Industry;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.service.IndustryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/industry")
@Tag(description = "Industry API", name = "industry controller")
public class IndustryController {

    @Autowired
    private IndustryService industryService;

    @PostMapping
    public GraceJSONResult createNode(@RequestBody Industry industry) {
        String name = industry.getName();
        boolean industryIsExistByName = industryService.getIndustryIsExistByName(name);
        if (!industryIsExistByName) {
            industryService.createIndustry(industry);
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.INDUSTRY_EXIST);
        }

        return GraceJSONResult.OK();
    }

    @GetMapping("/toplist")
    public GraceJSONResult getTopList() {
        List<String> topList = industryService.getTopList();
        return GraceJSONResult.OK(topList);
    }


    @GetMapping("/children/{industryId}")
    public GraceJSONResult getChildren(@PathVariable Long industryId) {
        List<String> childrenIndustryList = industryService.getChildrenIndustryList(industryId);
        return GraceJSONResult.OK(childrenIndustryList);
    }

}
