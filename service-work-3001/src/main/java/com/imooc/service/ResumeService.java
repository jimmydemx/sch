package com.imooc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.vo.Resume;

public interface ResumeService extends IService<Resume> {
    /**
     *  用戶注冊的時候初始化简历
      * @param userId
     */
    public void initResume(String userId);
}
