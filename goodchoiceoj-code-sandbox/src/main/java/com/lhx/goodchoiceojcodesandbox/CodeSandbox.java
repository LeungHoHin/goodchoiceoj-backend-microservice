package com.lhx.goodchoiceojcodesandbox;


import com.lhx.goodchoiceojcodesandbox.model.ExecuteCodeRequest;
import com.lhx.goodchoiceojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;


@Component
public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeRequest);

}
