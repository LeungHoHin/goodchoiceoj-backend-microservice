package com.lhx.judgeservice.judge.codesandbox;


import com.lhx.model.codesandbox.ExecuteCodeRequest;
import com.lhx.model.codesandbox.ExecuteCodeResponse;

public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeRequest);

}
