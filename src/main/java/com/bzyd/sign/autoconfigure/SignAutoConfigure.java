package com.bzyd.sign.autoconfigure;

import com.bzyd.sign.autoconfigure.properties.SignProperties;
import com.bzyd.sign.interceptor.SignInterceptor;
import com.bzyd.sign.interfaces.SignHelper;
import com.bzyd.sign.interfaces.SignSecretConfigHolder;
import com.bzyd.sign.interfaces.impl.DefaultSignHelper;
import com.bzyd.sign.interfaces.impl.DefaultSignSecretConfigHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

import static com.bzyd.sign.common.constants.SignConstant.CONFIG_PREFIX;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(SignInterceptor.class)
@EnableConfigurationProperties(SignProperties.class)
@ConditionalOnProperty(prefix = CONFIG_PREFIX,value = "enabled",havingValue = "true")
public class SignAutoConfigure implements WebMvcConfigurer {

    @Autowired
    private SignProperties signProperties;

    @Autowired(required = false)
    private SignHelper signHelper;

    @Autowired(required = false)
    private List<SignSecretConfigHolder> signSecretConfigHolders;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (signHelper == null){
            signHelper = DefaultSignHelper.getInstance();
        }

        if (signSecretConfigHolders == null){
            signSecretConfigHolders = new ArrayList<>();
        }
        signSecretConfigHolders.add(DefaultSignSecretConfigHolder.getInstance(signProperties.getSecret()));

        registry.addInterceptor(new SignInterceptor(signProperties.getDefaultContentType(),signHelper,signSecretConfigHolders))
                .addPathPatterns(signProperties.getIncludePaths())
                .excludePathPatterns(signProperties.getExcludePaths());
    }


}
