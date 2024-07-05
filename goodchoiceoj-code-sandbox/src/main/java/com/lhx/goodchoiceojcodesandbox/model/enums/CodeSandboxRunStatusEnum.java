package com.lhx.goodchoiceojcodesandbox.model.enums;


import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public enum CodeSandboxRunStatusEnum {
    NORMAL("正常运行", 1),
    COMPILE_FAILED("编译错误", 2),
    CODE_ERROR("用户代码错误", 3),
    SANDBOX_ERROR("沙箱内部错误", 4);

    private final String text;
    private final Integer value;

    CodeSandboxRunStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }


    /**
     * 获取值列表
     *
     * @return 值列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }


    /**
     * 根据 value 获取枚举
     *
     * @param value 传入的值
     * @return 所需的枚举值
     */
    public static CodeSandboxRunStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (CodeSandboxRunStatusEnum anEnum : CodeSandboxRunStatusEnum.values()) {
            if (Objects.equals(anEnum.getValue(), value)) {
                return anEnum;
            }
        }
        return null;
    }

}
