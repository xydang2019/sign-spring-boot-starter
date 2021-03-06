package com.bzyd.sign.common.model.VO;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiParamsForm implements Serializable {
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
    private String body;

    /**
     * 客户端id
     */
    private String appId;

    public <T>T getBody(Class<T> t) {
        return JSON.parseObject(body,t);
    }
}
