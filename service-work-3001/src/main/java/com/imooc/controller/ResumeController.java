package com.imooc.controller;


import com.imooc.bo.InitRequest;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.service.ResumeService;
import io.seata.core.context.RootContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resume")
@Tag(name = "resume", description = "resume apis")
@Slf4j
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    /**
     * @param request
     * @return
     */
    @PostMapping("/init")
    @Operation(summary = "Initiate resume", description = "Initiate resume")
    public GraceJSONResult init(@RequestBody InitRequest request) {
        String userId = request.getUserId();
        resumeService.initResume(userId);
        System.out.println("当前 XID = " + RootContext.getXID());
        return GraceJSONResult.OK();
    }
}
