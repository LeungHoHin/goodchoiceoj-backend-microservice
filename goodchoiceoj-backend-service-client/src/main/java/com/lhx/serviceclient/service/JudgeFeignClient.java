package com.lhx.serviceclient.service;


import com.lhx.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 判题服务
 */


@FeignClient("goodchoiceoj-backend-judge-service")
public interface JudgeFeignClient {


    @PostMapping("/inner/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
