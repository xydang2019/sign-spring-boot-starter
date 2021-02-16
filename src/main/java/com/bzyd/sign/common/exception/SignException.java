package com.bzyd.sign.common.exception;

public class SignException extends RuntimeException {
    private int code = 2001;
    private String msg = "签名错误";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
