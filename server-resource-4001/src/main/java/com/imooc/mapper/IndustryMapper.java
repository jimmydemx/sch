package com.imooc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.db.Industry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IndustryMapper extends BaseMapper<Industry> {

    @Select("SELECT name FROM Industry " +
            "WHERE father_id IN (SELECT id from Industry " +
            "WHERE father_id = #{industryId}) ORDER BY sort ASC")
    public List<String> getGrandChildrenIndustryList(Long industryId);
}
