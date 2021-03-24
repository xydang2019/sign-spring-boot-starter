package com.bzyd.sign.interfaces.impl;

import com.bzyd.sign.common.model.BO.SignSecretConfig;
import com.bzyd.sign.interfaces.SignSecretConfigHolder;

import java.util.List;

/**
 * 默认签名密钥持有器
 */
public class DefaultSignSecretConfigHolder implements SignSecretConfigHolder {

    private List<SignSecretConfig> signSecretConfigList;

    public DefaultSignSecretConfigHolder(List<SignSecretConfig> signSecretConfigList) {
        this.signSecretConfigList = signSecretConfigList;
    }

    @Override
    public List<SignSecretConfig> getSignSecretConfig() {
        return this.signSecretConfigList;
    }
}
