package com.lhx.judgeservice.judge.strategy;



import com.lhx.judgeservice.codesandbox.JudgeInfo;
import com.lhx.judgeservice.dto.question.JudgeCase;
import com.lhx.judgeservice.entity.Question;
import com.lhx.judgeservice.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

    private Integer status;

}
