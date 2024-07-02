package com.lhx.judgeservice.judge.codesandbox.impl;


import com.lhx.judgeservice.codesandbox.ExecuteCodeRequest;
import com.lhx.judgeservice.codesandbox.ExecuteCodeResponse;
import com.lhx.judgeservice.judge.codesandbox.CodeSandbox;

public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeRequest) {
        System.out.println("第三方代码沙箱");

        return null;
    }
}
