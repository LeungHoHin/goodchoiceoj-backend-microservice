package com.lhx.judgeservice.judge.controller.inner;

import com.lhx.judgeservice.judge.JudgeService;
import com.lhx.model.entity.QuestionSubmit;
import com.lhx.serviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

public class JudgeInnerController implements JudgeFeignClient {
    @Resource
    private JudgeService judgeService;


    @Override
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
