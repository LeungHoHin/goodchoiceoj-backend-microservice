package com.lhx.judgeservice.judge.codesandbox;


import com.lhx.judgeservice.judge.codesandbox.impl.ExampleCodeSandbox;
import com.lhx.judgeservice.judge.codesandbox.impl.RemoteSandbox;
import com.lhx.judgeservice.judge.codesandbox.impl.ThirdPartyCodeSandbox;

public class CodeSandboxFactory {

    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}
