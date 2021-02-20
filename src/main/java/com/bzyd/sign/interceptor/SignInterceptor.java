package com.bzyd.sign.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.bzyd.sign.common.annotation.AuthPassport;
import com.bzyd.sign.common.enums.SignEnum;
import com.bzyd.sign.common.exception.SignException;
import com.bzyd.sign.common.model.BO.SignSecretConfig;
import com.bzyd.sign.interfaces.SignHelper;
import com.bzyd.sign.interfaces.SignSecretConfigHolder;
import com.bzyd.sign.util.RequestUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 签名拦截器
 */
public class SignInterceptor implements HandlerInterceptor {

    //默认Content-Type
    private String defaultContentType;

    //签名帮助类
    private SignHelper signHelper;

    //签名密钥，key为appId，value为secretKey
    private Map<String, String> signSecretMap;

    public SignInterceptor(String defaultContentType, SignHelper signHelper, List<SignSecretConfigHolder> signSecretConfigHolders) {
        this.defaultContentType = defaultContentType;
        this.signHelper = signHelper;
        this.signSecretMap = new HashMap<>();
        if (signSecretConfigHolders != null && !signSecretConfigHolders.isEmpty()) {
            signSecretConfigHolders.forEach(signSecretConfigHolder -> {
                List<SignSecretConfig> signSecretConfigList = signSecretConfigHolder.getSignSecretConfig();
                if (signSecretConfigList != null && !signSecretConfigList.isEmpty()) {
                    for (SignSecretConfig signSecretConfig : signSecretConfigList) {
                        this.signSecretMap.put(signSecretConfig.getAppId(), signSecretConfig.getSecretKey());
                    }
                }
            });
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            AuthPassport authPassport = ((HandlerMethod) handler).getMethodAnnotation(AuthPassport.class);
            //有标记@AuthPassport注解时优先按照注解中的类型进行校验，
            //没有则按照默认Content-Type进行校验
            if ((authPassport != null && authPassport.type().equals(SignEnum.FORM))
                    || SignEnum.FORM.getTag().equals(defaultContentType)) {
                JSONObject params = RequestUtil.getFormReqParams(request);
                String appId = params.getString("appId");
                String secretKey = this.signSecretMap.get(appId);
                if (signHelper.checkSign(params, secretKey)) {
                    return true;
                } else {
                    throw new SignException();
                }
            }

            if ((authPassport != null && authPassport.type().equals(SignEnum.JSON))
                    || SignEnum.JSON.getTag().equals(defaultContentType)) {


            }
        }
        return false;
    }

}
