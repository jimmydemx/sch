package com.imooc.controller;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.service.CategoryService;
import com.imooc.service.impl.CategoryServiceImpl;
import com.imooc.vo.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "示例API", description = "这是一个示例接口")
public class hello {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/hello")
    @Operation(summary = "问候接口", description = "返回一个欢迎信息")
    private String helloWord(){
        return "hello world, service-user";
    }



    @GetMapping("/categories")
    @Operation(summary ="食物分类",description = "返回食物分类列表")
    private GraceJSONResult getAllCategories(){
        List<Category> allCategories = categoryService.getAllCategories();

        return GraceJSONResult.OK(allCategories);
    }




}


