package com.bzyd.sign.common.enums;

/**
 * 签名枚举类
 */
public enum SignEnum {

    /**
     * 以application/x-www-form-urlencoded的形式传输
     */
    FORM("application/x-www-form-urlencoded"),

    /**
     * 以application/json的形式传输
     */
    JSON("application/json");

    private final String tag;

    SignEnum(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }}
