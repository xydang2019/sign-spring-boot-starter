package com.bzyd.sign.interfaces;

import com.bzyd.sign.common.model.BO.SignSecretConfig;

import java.util.List;

/**
 * 签名密钥持有器接口
 */
public interface SignSecretConfigHolder {

    /**
     * 获取签名密钥配置信息
     * @return
     */
    List<SignSecretConfig> getSignSecretConfig();

}
