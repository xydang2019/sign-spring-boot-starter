package com.bzyd.sign.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 请求工具类
 */
public class RequestUtil {

    /**
     * 获取请求参数(application/x-www-form-urlencoded方式)
     *
     * @param request
     * @return
     */
    public static JSONObject getFormReqParams(HttpServletRequest request) {

        Map properties = request.getParameterMap();
        JSONObject result = new JSONObject();
        Iterator entries = properties.entrySet().iterator();
        Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Entry) entries.next();
            name = (String) entry.getKey();

            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }

            result.put(name, value);
        }
        return result;
    }

    /**
     * 获取请求参数(application/json方式)
     *
     * @param request
     * @return
     */
    public static JSONObject getJsonReqParams(HttpServletRequest request) {
        JSONObject params = null;
        try {
            params = JSON.parseObject(request.getInputStream(), Charset.forName("UTF-8"), JSONObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return params;
    }
}
