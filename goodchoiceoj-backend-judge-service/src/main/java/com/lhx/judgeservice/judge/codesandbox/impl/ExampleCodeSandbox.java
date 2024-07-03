package com.lhx.judgeservice.judge.codesandbox.impl;



import com.lhx.judgeservice.judge.codesandbox.CodeSandbox;
import com.lhx.model.codesandbox.ExecuteCodeRequest;
import com.lhx.model.codesandbox.ExecuteCodeResponse;
import com.lhx.model.codesandbox.JudgeInfo;
import com.lhx.model.enums.JudgeInfoMessageEnum;
import com.lhx.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
