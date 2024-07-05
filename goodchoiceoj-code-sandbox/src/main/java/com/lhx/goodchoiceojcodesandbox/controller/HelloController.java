package com.lhx.goodchoiceojcodesandbox.controller;


import com.lhx.goodchoiceojcodesandbox.CodeSandbox;
import com.lhx.goodchoiceojcodesandbox.JavaDockerCodeSandbox;
import com.lhx.goodchoiceojcodesandbox.model.ExecuteCodeRequest;
import com.lhx.goodchoiceojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController("/")
public class HelloController {

    private static final String AUTH_REQUEST_HEADER = "LiangHaoxuan";

    private static final String AUTH_REQUEST_SECRET = "LeungHohin";


    @Resource
    CodeSandbox javaDockerCodeSandbox = new JavaDockerCodeSandbox();

    @GetMapping("/health")
    public String hello() {
        return "Hello world!";
    }


    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
        if (!AUTH_REQUEST_SECRET.equals(authHeader)) {
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        return javaDockerCodeSandbox.executeCode(executeCodeRequest);
    }
}


