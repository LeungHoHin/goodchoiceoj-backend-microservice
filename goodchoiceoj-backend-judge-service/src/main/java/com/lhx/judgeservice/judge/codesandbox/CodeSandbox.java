package com.lhx.judgeservice.judge.codesandbox;


import com.lhx.judgeservice.codesandbox.ExecuteCodeRequest;
import com.lhx.judgeservice.codesandbox.ExecuteCodeResponse;

public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeRequest);

}
