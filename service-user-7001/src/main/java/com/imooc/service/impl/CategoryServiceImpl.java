package com.imooc.service.impl;

import com.imooc.mapper.CategoryMapper;
import com.imooc.service.CategoryService;
import com.imooc.vo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> getAllCategories() {
        String sql = "SELECT * from category";
        return jdbcTemplate.query(sql,new CategoryMapper());

    }
}
