package com.bzyd.sign.autoconfigure;

import com.bzyd.sign.autoconfigure.properties.SignProperties;
import com.bzyd.sign.filter.RequestBodyReadFilter;
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
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
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

    private SignProperties signProperties;

    private SignHelper signHelper;

    private List<SignSecretConfigHolder> signSecretConfigHolders;

    @Autowired(required = false)
    public SignAutoConfigure(SignHelper signHelper, List<SignSecretConfigHolder> signSecretConfigHolders, SignProperties signProperties) {
        this.signHelper = signHelper;
        this.signSecretConfigHolders = signSecretConfigHolders;
        this.signProperties = signProperties;
    }

    //配置签名拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (signHelper == null){
            signHelper = new DefaultSignHelper();
        }

        if (signSecretConfigHolders == null){
            signSecretConfigHolders = new ArrayList<>();
        }
        signSecretConfigHolders.add(new DefaultSignSecretConfigHolder(signProperties.getSecret()));

        registry.addInterceptor(new SignInterceptor(signProperties.getDefaultContentType(),signHelper,signSecretConfigHolders))
                .addPathPatterns(signProperties.getIncludePaths())
                .excludePathPatterns(signProperties.getExcludePaths());
    }


    //配置请求体读取过滤器
    @Bean
    public FilterRegistrationBean getFilterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new RequestBodyReadFilter(signProperties.getExcludePaths()));
        bean.setName("RequestBodyReadFilter");
        String[] strArr = signProperties.getIncludePaths();
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = strArr[i].replace("**","*");
        }
        bean.addUrlPatterns(strArr);
        return bean;
    }
}
