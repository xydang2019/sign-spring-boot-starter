package com.bzyd.sign.autoconfigure.properties;

import com.bzyd.sign.common.model.BO.SignSecretConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

import static com.bzyd.sign.common.constants.SignConstant.CONFIG_PREFIX;

@Data
@ConfigurationProperties(prefix = CONFIG_PREFIX)
public class SignProperties {

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 验证路径
     */
    private String[] includePaths;

    /**
     * 排除路径
     */
    private String[] excludePaths;

    /**
     * 签名密钥
     */
    @NestedConfigurationProperty
    private List<SignSecretConfig> secret;


}
