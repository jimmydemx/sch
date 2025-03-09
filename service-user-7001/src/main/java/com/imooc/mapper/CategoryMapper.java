package com.imooc.mapper;


import com.imooc.vo.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CategoryMapper implements RowMapper<Category> {
    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setType(rs.getInt("type"));
        category.setName(rs.getString("name"));
        category.setSort(rs.getInt("sort"));
        category.setStatus(rs.getInt("status"));

        // 处理时间字段
        Timestamp createTimestamp = rs.getTimestamp("create_time");
        if (createTimestamp != null) {
            category.setCreateTime(createTimestamp.toLocalDateTime());
        }

        Timestamp updateTimestamp = rs.getTimestamp("update_time");
        if (updateTimestamp != null) {
            category.setUpdateTime(updateTimestamp.toLocalDateTime());
        }

        category.setCreateUser(rs.getInt("create_user"));
        category.setUpdateUser(rs.getInt("update_user"));

        return category;
    }

    }


