package com.lhx.judgeservice.judge.codesandbox;


import com.lhx.model.codesandbox.ExecuteCodeRequest;
import com.lhx.model.codesandbox.ExecuteCodeResponse;
import org.springframework.stereotype.Service;


public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeRequest);

}
