package com.lhx.serviceclient.service;

import com.lhx.model.entity.Question;
import com.lhx.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Neveremoer
 * @description 针对表【question(题目)】的数据库操作Service
 * @createDate 2024-04-30 23:04:45
 */


@FeignClient(value = "goodchoiceoj-backend-question-service")
public interface QuestionFeignClient {
    @GetMapping("/inner/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    @GetMapping("/inner/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId);

    @PostMapping("/inner/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

    @PostMapping("/inner/question/update")
    boolean updateQuestion(@RequestBody Question question);

}
