package com.imooc.controller;


import com.imooc.base.BaseInfoProperties;
import com.imooc.db.Industry;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.service.IndustryService;
import com.imooc.utils.GsonUtils;
import com.imooc.utils.RedisOperators;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/industry")
@Tag(description = "Industry API", name = "industry controller")
public class IndustryController extends BaseInfoProperties {

    @Autowired
    private RedisOperators redisOperators;

    @Autowired
    private IndustryService industryService;


    @GetMapping("app/initToplist")
    public GraceJSONResult initToplist() {
        return GraceJSONResult.OK(industryService.getTopList());
    }


    @GetMapping("app/getThirdListByTop/{topIndustryId}")
    public GraceJSONResult getThirdListByTop(@PathVariable Long topIndustryId) {


        // REDIS 查询
        String key = REDIS_COMPANY_BASE_INFO + "_" + topIndustryId;
        String value = redisOperators.getValue(key);
        if (value == null) {
            List<String> thirdListByTop = industryService.getThirdListByTop(topIndustryId);
            redisOperators.setString(key, GsonUtils.object2String(thirdListByTop));
            return GraceJSONResult.OK(thirdListByTop);
        }

        List<String> thirdListByTop = GsonUtils.stringToBean(value);

        log.info("Get from Redis TopList: {}", thirdListByTop);
        return GraceJSONResult.OK(thirdListByTop);
    }


    @GetMapping("/grandchilrenList")
    public GraceJSONResult getGrandChildrenIndustryList(Long industryId) {
        Industry industry = industryService.getById(industryId);
        List<String> grandChildrenIndustryList = industryService.getGrandChildrenIndustryList(industry);
        return GraceJSONResult.OK(grandChildrenIndustryList);
    }


    //=================================================APP end===================================//


    /**
     * @param industry
     * @return
     */
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


    @DeleteMapping("/deleteNode/{industryId}")
    public GraceJSONResult deleteNode(@PathVariable("industryId") Long industryId) {
        industryService.deleteIndustry(industryId);
        return GraceJSONResult.OK();
    }


    @PostMapping("update")
    public GraceJSONResult updateIndustryName(Long industryId, String newName) {
        Industry industry = industryService.getById(industryId);
        industryService.updateIndustryName(newName, industryId, industry.getLevel());
        return GraceJSONResult.OK();
    }

}
