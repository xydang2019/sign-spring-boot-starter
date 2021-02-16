package com.bzyd.sign.interfaces.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bzyd.sign.interfaces.SignHelper;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 默认的签名帮助类
 */
public class DefaultSignHelper implements SignHelper {

    private DateTimeFormatter df1;
    private DateTimeFormatter df2;

    private DefaultSignHelper() {
        df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        df2 = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    }

    private static volatile SignHelper signHelper = null;

    public static SignHelper getInstance(){
        if (signHelper == null){
            synchronized (DefaultSignHelper.class){
                if (signHelper == null){
                    signHelper = new DefaultSignHelper();
                }
            }
        }
        return signHelper;
    }

    @Override
    public String getSign(JSONObject body, String signKey) {

        //对参数进行ASCII码从小到大排序（字典序）
        List<Map.Entry<String, Object>> akeys = new ArrayList<>(body.entrySet());
        Collections.sort(akeys,new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        StringBuffer sb = new StringBuffer();
        sb.append(signKey + ":");
        for(Map.Entry<String, Object> item : akeys){
            String key = item.getKey();
            Object val = item.getValue();

            if(null != val && !"".equals(val.toString())
                    && !"sign".equals(key)) {
                sb.append(key + ":" + val.toString() + ":");
            }
        }

        sb.append(signKey);
        String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();
        return sign;
    }

    @Override
    public boolean checkSign(JSONObject params, String signKey) {

        try {
            if(params.get("sign") != null) {
                String sign = params.getString("sign");
                String time = params.getString("time");
                String bodyStr = params.getString("body");
                JSONObject body = JSON.parseObject(bodyStr);

                String signTime = df2.format(df1.parse(time));
                body.put("signTime", signTime);

                String _sign = getSign(body,signKey);
                if(_sign.equals(sign)){
                    return true;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}
