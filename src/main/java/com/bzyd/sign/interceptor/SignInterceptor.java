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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            AuthPassport authPassport = ((HandlerMethod) handler).getMethodAnnotation(AuthPassport.class);
            JSONObject params = null;//请求参数

            if (authPassport != null){
                if (authPassport.type().equals(SignEnum.FORM)) {
                    params = RequestUtil.getFormReqParams(request);
                }

                if (authPassport.type().equals(SignEnum.JSON)) {
                    params = RequestUtil.getJsonReqParams(request);
                }

            }else {
                if (SignEnum.FORM.getTag().equals(defaultContentType)) {
                    params = RequestUtil.getFormReqParams(request);
                }

                if (SignEnum.JSON.getTag().equals(defaultContentType)) {
                    params = RequestUtil.getJsonReqParams(request);
                }
            }

            if (params == null){
                throw new RuntimeException("Failed to get request parameters...");
            }

            String appId = params.getString("appId");
            String secretKey = this.signSecretMap.get(appId);
            if (signHelper.checkSign(params, secretKey)) {
                return true;
            } else {
                throw new SignException();
            }
        }
        return false;
    }

}
