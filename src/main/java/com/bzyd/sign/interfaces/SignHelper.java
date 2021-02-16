package com.bzyd.sign.interfaces;

import com.alibaba.fastjson.JSONObject;

/**
 * 签名帮助接口，需要自定义签名算法时实现此接口
 */
public interface SignHelper {

    /**
     * 获取签名
     * @param body 参数
     * @param signKey 密钥
     * @return
     */
    String getSign(JSONObject body, String signKey);

    /**
     * 验证签名
     * @param params 参数
     * @param signKey 密钥
     * @return
     */
    boolean checkSign(JSONObject params, String signKey);
}
