package com.imooc.controller;


import com.imooc.bo.InitRequest;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resume")
@Tag(name="resume",description = "resume apis")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;
    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/init")
    @Operation(summary = "Initiate resume",description = "Initiate resume")
    public GraceJSONResult init(@RequestBody InitRequest request) {
        String userId = request.getUserId();
        resumeService.initResume(userId);
        return GraceJSONResult.OK();
    }
}
