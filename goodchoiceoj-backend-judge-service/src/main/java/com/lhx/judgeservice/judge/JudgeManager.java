package com.lhx.judgeservice.judge;


import com.lhx.model.codesandbox.JudgeInfo;
import com.lhx.model.entity.QuestionSubmit;
import com.lhx.judgeservice.judge.strategy.DefaultJudgeStrategy;
import com.lhx.judgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.lhx.judgeservice.judge.strategy.JudgeContext;
import com.lhx.judgeservice.judge.strategy.JudgeStrategy;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
