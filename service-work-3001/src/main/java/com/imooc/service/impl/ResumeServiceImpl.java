package com.imooc.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.mapper.ResumeMapper;
import com.imooc.service.ResumeService;
import com.imooc.vo.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper,Resume>  implements ResumeService {

    @Autowired
    private ResumeMapper resumeMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initResume(String userId) {
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setCreateTime(LocalDateTime.now());
        resume.setUpdatedTime(LocalDateTime.now());
        resumeMapper.insert(resume);
    }
}
