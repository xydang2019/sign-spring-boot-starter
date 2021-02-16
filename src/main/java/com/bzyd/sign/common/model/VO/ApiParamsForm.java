package com.bzyd.sign.common.model.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * 以application/x-www-form-urlencoded形式传输的接口参数对象
 */
@Data
public class ApiParamsForm<T> implements Serializable {
    private static final long serialVersionUID = 7939627558300114670L;

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
    private String body;

    /**
     * 客户端id
     */
    private String appId;


}
