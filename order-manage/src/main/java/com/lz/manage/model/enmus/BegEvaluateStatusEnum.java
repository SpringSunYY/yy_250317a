package com.lz.manage.model.enmus;

import com.lz.common.utils.StringUtils;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project: config
 * Description: CBegEvaluateStatusEnum
 * 评估状态枚举（严格遵循v1.0生成规范）
 * Version: 1.0
 */
public enum BegEvaluateStatusEnum {

    STATUS_1("未知状态", "1"),
    STATUS_2("请求中", "2"),
    STATUS_3("长时未回复", "3"),
    STATUS_4("已请求成功", "4");

    private final String text;
    private final String value;
    private static final ConcurrentHashMap<String, BegEvaluateStatusEnum> VALUE_MAP = new ConcurrentHashMap<>();

    static {
        for (BegEvaluateStatusEnum item : values()) {
            VALUE_MAP.put(item.value, item);
        }
    }

    BegEvaluateStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    // 严格保持历史方法结构
    public String getStatusStyle() {
        return "default";  // 第六列数据均为default
    }

    public static Optional<BegEvaluateStatusEnum> getEnumByValue(String value) {
        return Optional.ofNullable(VALUE_MAP.get(StringUtils.trimToEmpty(value)));
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}