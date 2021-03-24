package com.bzyd.sign.common.model.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiParamsJson<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 签名
     */
    private String sign;

    /**
     * 时间字符串
     */
    private String time;

    /**
     * 请求参数
     */
    private T body;

    /**
     * 客户端id
     */
    private String appId;
}
