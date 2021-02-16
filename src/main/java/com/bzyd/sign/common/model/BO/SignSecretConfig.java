package com.bzyd.sign.common.model.BO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 签名密钥
 */
@Data
@Accessors(chain = true)
public class SignSecretConfig {

    /**
     * 客户端id
     */
    private String appId;

    /**
     * 签名密钥
     */
    private String secretKey;

}
