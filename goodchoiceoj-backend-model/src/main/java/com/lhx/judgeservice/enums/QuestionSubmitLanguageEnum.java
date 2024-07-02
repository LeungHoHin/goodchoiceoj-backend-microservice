package com.lhx.judgeservice.enums;


import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum QuestionSubmitLanguageEnum {
    JAVA("java", "java"),
    CPLUSPLUS("cpp", "c++"),
    GOLANG("go", "go"),
    PYTHON("python", "python"),
    C("c", "c");


    private final String text;

    private final String value;

    QuestionSubmitLanguageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }


    /**
     * 获取值列表
     *
     * @return 值列表
     */
    public static List<String> getValue() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public static QuestionSubmitLanguageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitLanguageEnum anEnum : QuestionSubmitLanguageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
