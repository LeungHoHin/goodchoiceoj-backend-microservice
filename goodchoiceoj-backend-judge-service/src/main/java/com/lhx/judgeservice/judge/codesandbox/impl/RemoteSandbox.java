package com.lhx.judgeservice.judge.codesandbox.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import com.lhx.common.common.ErrorCode;
import com.lhx.common.exception.BusinessException;
import com.lhx.judgeservice.judge.codesandbox.CodeSandbox;
import com.lhx.model.codesandbox.ExecuteCodeRequest;
import com.lhx.model.codesandbox.ExecuteCodeResponse;
import org.springframework.beans.factory.annotation.Value;

public class RemoteSandbox implements CodeSandbox {


    private static final String AUTH_REQUEST_HEADER = "LiangHaoxuan";

    private static final String AUTH_REQUEST_SECRET = "LeungHohin";

    @Value("${codesandbox.address}")
    private String REMOTE_SANDBOX_URL;

//    private static final String REMOTE_SANDBOX_URL = "http://106.53.68.162:8091/executeCode";


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeRequest) {
        System.out.println("远程代码沙箱");
        String json = JSONUtil.toJsonStr(executeRequest);
        String responseStr = HttpUtil.createPost(REMOTE_SANDBOX_URL)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StrUtil.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "远程代码沙箱执行错误：" + responseStr);
        }
        ExecuteCodeResponse response = JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
        return response;
    }
}
