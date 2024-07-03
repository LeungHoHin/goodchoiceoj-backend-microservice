package com.lhx.questionservice.controller.inner;

import com.lhx.model.entity.Question;
import com.lhx.model.entity.QuestionSubmit;
import com.lhx.questionservice.service.QuestionService;
import com.lhx.questionservice.service.QuestionSubmitService;
import com.lhx.serviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Override
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }

    @Override
    public boolean updateQuestion(Question question) {
        return questionService.updateById(question);
    }
}
