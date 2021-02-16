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

    //签名帮助类
    private SignHelper signHelper;

    //签名密钥，key为appId，value为secretKey
    private Map<String,String> signSecretMap;

    public SignInterceptor(SignHelper signHelper, List<SignSecretConfigHolder> signSecretConfigHolders) {
        this.signHelper = signHelper;
        signSecretMap = new HashMap<>();
        if (signSecretConfigHolders != null && !signSecretConfigHolders.isEmpty()){
            signSecretConfigHolders.forEach(signSecretConfigHolder -> {
                List<SignSecretConfig> signSecretConfigList = signSecretConfigHolder.getSignSecretConfig();
                if (signSecretConfigList != null && !signSecretConfigList.isEmpty()){
                    for (SignSecretConfig signSecretConfig : signSecretConfigList) {
                        signSecretMap.put(signSecretConfig.getAppId(),signSecretConfig.getSecretKey());
                    }
                }
            });
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
            AuthPassport authPassport = ((HandlerMethod) handler).getMethodAnnotation(AuthPassport.class);
            if(authPassport == null || authPassport.type().equals(SignEnum.FORM)){
                //当没有标记@AuthPassport注解时，
                //当标记@AuthPassport注解，且类型为SignEnum.FORM时
                //按照application/x-www-form-urlencoded类型进行校验
                JSONObject params = RequestUtil.getFormReqParams(request);
                String appId = params.getString("appId");
                String secretKey = getSecretKey(appId);
                if(signHelper.checkSign(params,secretKey)){
                    return true;
                }
                throw new SignException();
            }else if (authPassport.type().equals(SignEnum.JSON)){
                //当标记@AuthPassport注解，且类型为SignEnum.JSON时
                //按照application/json类型进行校验

            }else if (authPassport.type().equals(SignEnum.NO_SIGN)){
                //当标记@AuthPassport注解，且类型为SignEnum.NO_SIGN时
                //不进行签名检验，直接放行
                return true;
            }
        }
        return true;
    }

    /**
     * 根据appId获取secretKey
     * @param appId
     * @return
     */
    private String getSecretKey(String appId) {
        return signSecretMap.get(appId);
    }
}
