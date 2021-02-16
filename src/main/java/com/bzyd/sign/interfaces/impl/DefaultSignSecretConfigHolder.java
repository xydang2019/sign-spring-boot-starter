package com.bzyd.sign.interfaces.impl;

import com.bzyd.sign.common.model.BO.SignSecretConfig;
import com.bzyd.sign.interfaces.SignSecretConfigHolder;

import java.util.List;

/**
 * 默认签名密钥持有器
 */
public class DefaultSignSecretConfigHolder implements SignSecretConfigHolder {

    private List<SignSecretConfig> signSecretConfigList;

    private DefaultSignSecretConfigHolder(List<SignSecretConfig> signSecretConfigList) {
        this.signSecretConfigList = signSecretConfigList;
    }

    private static volatile SignSecretConfigHolder signSecretConfigHolder = null;

    public static SignSecretConfigHolder getInstance(List<SignSecretConfig> signSecretConfigList){
        if (signSecretConfigHolder == null){
            synchronized (DefaultSignSecretConfigHolder.class){
                if (signSecretConfigHolder == null){
                    signSecretConfigHolder = new DefaultSignSecretConfigHolder(signSecretConfigList);
                }
            }
        }
        return signSecretConfigHolder;
    }


    @Override
    public List<SignSecretConfig> getSignSecretConfig() {
        return this.signSecretConfigList;
    }
}
