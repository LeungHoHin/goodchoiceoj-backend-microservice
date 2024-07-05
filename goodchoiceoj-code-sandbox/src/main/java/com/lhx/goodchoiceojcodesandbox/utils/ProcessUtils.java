package com.lhx.goodchoiceojcodesandbox.utils;

import com.lhx.goodchoiceojcodesandbox.model.ExecuteCodeRequest;
import com.lhx.goodchoiceojcodesandbox.model.ExecuteMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessUtils {
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            //等待程序执行，获取错误码
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);

            //正常退出
            if (exitValue == 0) {
                System.out.println(opName + "成功");
                //分批获取进程的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                ArrayList<String> outputStringList = new ArrayList<>();
                //逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputStringList.add(compileOutputLine);
                }
                executeMessage.setMessage(StringUtils.join(outputStringList, "\n"));
            }
            //异常退出
            else {
                System.out.println(opName + "失败，错误码： " + exitValue);
                //分批获取进程的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                ArrayList<String> outputStringList = new ArrayList<>();
                //逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputStringList.add(compileOutputLine);
                }
                executeMessage.setMessage(StringUtils.join(outputStringList, "\n"));

                //分批获取进程的错误输出
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                //逐行读取
                List<String> errorOutputStringList = new ArrayList<>();
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                    errorOutputStringList.add(errorCompileOutputLine);
                }
//                if (!ObjectUtils.isEmpty(executeMessage)){
//                    executeMessage.setErrorMessage(StringUtils.join(errorOutputStringList));
//                }
            }
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }
}
