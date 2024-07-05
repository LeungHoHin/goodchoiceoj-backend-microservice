package com.lhx.goodchoiceojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.WordTree;
import com.lhx.goodchoiceojcodesandbox.model.ExecuteCodeRequest;
import com.lhx.goodchoiceojcodesandbox.model.ExecuteCodeResponse;
import com.lhx.goodchoiceojcodesandbox.model.ExecuteMessage;
import com.lhx.goodchoiceojcodesandbox.model.JudgeInfo;
import com.lhx.goodchoiceojcodesandbox.model.enums.CodeSandboxRunStatusEnum;
import com.lhx.goodchoiceojcodesandbox.utils.ProcessUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Deprecated
public class DeprecatedJavaNativeCodeSandbox implements CodeSandbox {

    private static final String GLOBAL_CODE_DIR_NAME = "tempCodes";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final WordTree WORD_TREE;

    /**
     * 为了确保安全的执行程序内的关键字黑名单
     */
    private static final List<String> blackList = Arrays.asList("Files", "exec");

    static {
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(blackList);
    }

//    public static void main(String[] args) {
//        DeprecatedJavaNativeCodeSandbox javaNativeCodeSandbox = new DeprecatedJavaNativeCodeSandbox();
//        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
//        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3"));
//        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
//        executeCodeRequest.setCode(code);
//        executeCodeRequest.setLanguage("java");
//        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
//        System.out.println(executeCodeResponse);
//    }


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeRequest) {
        List<String> inputList = executeRequest.getInputList();
        String code = executeRequest.getCode();
        String language = executeRequest.getLanguage();

        //1. 把用户的代码保存为文件
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        //把用户的代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);


        //2. 编译代码，得到 class 文件
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());

        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
        } catch (IOException e) {
            return getErrorResponse(e);
        }

        //3. 执行代码，得到输出结果
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);

            //执行代码
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                //超时控制
                new Thread(() -> {
                    try {
                        Thread.sleep(10000L);
                        System.out.println("运行超时");
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                return getErrorResponse(e);
            }
        }

        //4. 收集整理输出结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        ArrayList<String> outputList = new ArrayList<>();
        //取用时最大值，便于判断是否超时
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                //用户提交的代码执行中存在错误
                executeCodeResponse.setStatus(CodeSandboxRunStatusEnum.CODE_ERROR.getValue());
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(time, maxTime);
            }
        }
        //正常运行完成
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(CodeSandboxRunStatusEnum.NORMAL.getValue());
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);

        executeCodeResponse.setJudgeInfo(judgeInfo);

        //5. 文件清理
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
        }
        return executeCodeResponse;
    }


    /**
     * 获取错误相应
     *
     * @param e 错误
     * @return 错误相应
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        //表示代码沙箱错误
        executeCodeResponse.setStatus(CodeSandboxRunStatusEnum.SANDBOX_ERROR.getValue());
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
