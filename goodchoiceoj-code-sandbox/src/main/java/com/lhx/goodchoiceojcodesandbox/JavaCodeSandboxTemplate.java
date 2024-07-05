package com.lhx.goodchoiceojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.lhx.goodchoiceojcodesandbox.model.ExecuteCodeRequest;
import com.lhx.goodchoiceojcodesandbox.model.ExecuteCodeResponse;
import com.lhx.goodchoiceojcodesandbox.model.ExecuteMessage;
import com.lhx.goodchoiceojcodesandbox.model.JudgeInfo;
import com.lhx.goodchoiceojcodesandbox.model.enums.CodeSandboxRunStatusEnum;
import com.lhx.goodchoiceojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
public class JavaCodeSandboxTemplate implements CodeSandbox {


    private static final String GLOBAL_CODE_DIR_NAME = "tempCodes";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final long TIME_OUT = 5000L;


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeRequest) {
        List<String> inputList = executeRequest.getInputList();
        String code = executeRequest.getCode();
        String language = executeRequest.getLanguage();

        //1. 把用户的代码保存为文件
        File userCodeFile = saveCodeToFile(code);


        //2. 编译代码，得到 class 文件
        ExecuteMessage compileExecuteMessage = compileFile(userCodeFile);
        //编译错误的措施
        if (compileExecuteMessage.getExitValue() == 1) {
            ExecuteCodeResponse compileFailedResponse = new ExecuteCodeResponse();
            compileFailedResponse.setOutputList(null);
            compileFailedResponse.setMessage(compileExecuteMessage.getMessage());
            //编译错误
            compileFailedResponse.setStatus(CodeSandboxRunStatusEnum.COMPILE_FAILED.getValue());
            JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setMemory(0L);
            judgeInfo.setMessage("编译错误");
            judgeInfo.setTime(0L);
            compileFailedResponse.setJudgeInfo(judgeInfo);
            boolean isDeleted = deleteFile(userCodeFile);
            if (!isDeleted) {
                log.error("用户代码执行完后删除失败", userCodeFile.getAbsolutePath());
            }
            return compileFailedResponse;
        }

        //3. 执行代码，得到输出结果
//        List<ExecuteMessage> executeMessagesList = null;
//        final ExecutorService exec = Executors.newFixedThreadPool(1);
//        Callable<List<ExecuteMessage>> call = new Callable<List<ExecuteMessage>>() {
//            @Override
//            public List<ExecuteMessage> call() throws Exception {
//                return runFile(userCodeFile, inputList);
//            }
//        };
//        try {
//            Future<List<ExecuteMessage>> future = exec.submit(call);
//            executeMessagesList = future.get(5, TimeUnit.SECONDS);
//        }catch (Exception e){
//            System.out.println("执行代码超时");
//        }
        List<ExecuteMessage> executeMessagesList = runFile(userCodeFile, inputList);

        //4. 收集整理输出结果
        ExecuteCodeResponse executeCodeResponse = getOutputResponse(executeMessagesList);

        //5. 文件清理
        boolean isDeleted = deleteFile(userCodeFile);
        if (!isDeleted) {
            log.error("用户代码执行完后删除失败", userCodeFile.getAbsolutePath());
        }
        return executeCodeResponse;
    }

    /**
     * 1. 把用户的代码保存为文件
     *
     * @param code 用户代码
     * @return
     */
    public File saveCodeToFile(String code) {
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        // 判断全局代码目录是否存在，没有则新建
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        // 把用户的代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }

    /**
     * 2、编译代码
     *
     * @param userCodeFile
     * @return
     */
    public ExecuteMessage compileFile(File userCodeFile) {
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            return executeMessage;
        } catch (Exception e) {
//            return getErrorResponse(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 3、执行文件，获得执行结果列表
     *
     * @param userCodeFile
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                // 超时控制
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        System.out.println("超时了，中断");
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                throw new RuntimeException("执行错误", e);
            }
        }
        return executeMessageList;
    }

    /**
     * 4、获取输出结果
     *
     * @param executeMessageList
     * @return
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        // 取用时最大值，便于判断是否超时
        long maxTime = 0;
        long maxMemory = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                // 用户提交的代码执行中存在错误
                executeCodeResponse.setStatus(CodeSandboxRunStatusEnum.CODE_ERROR.getValue());
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            Long memory = executeMessage.getMemory();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
            if (memory != null) {
                maxMemory = Math.max(maxMemory, memory);
            }
        }
        // 正常运行完成
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(CodeSandboxRunStatusEnum.NORMAL.getValue());
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        // 要借助第三方库来获取内存占用，非常麻烦，此处不做实现
        judgeInfo.setMemory(maxMemory);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }

    /**
     * 5、删除文件
     *
     * @param userCodeFile
     * @return
     */
    public boolean deleteFile(File userCodeFile) {
        if (userCodeFile.getParentFile() != null) {
            String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
            return del;
        }
        return true;
    }

    /**
     * 6、获取错误响应
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(CodeSandboxRunStatusEnum.SANDBOX_ERROR.getValue());
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }

}



