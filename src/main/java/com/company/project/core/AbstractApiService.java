package com.company.project.core;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.project.constant.StringConstant;
import com.company.project.util.HttpUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public abstract class AbstractApiService implements ApiService {

    /**
     * post请求
     */
    public <T> T doPost(String url, Object params, Class<T> clazz) {
        String strResp = "";
        try {
            CloseableHttpResponse resp = HttpUtil.postJson(StringConstant.API_HOST + url, JSON.toJSONString(params));
            System.out.println(JSON.toJSONString(params));
            strResp = EntityUtils.toString(resp.getEntity(), "UTF-8");
            System.out.println(strResp);
        } catch (Exception e) {
            throw new ServiceException(StringConstant.REMOTE_ERROR);
        }
        if (StringUtils.isNotBlank(strResp)) {
            JSONObject jsonobject = JSON.parseObject(strResp);
            return this.doresult(jsonobject, clazz);
        } else {
            throw new ServiceException(StringConstant.REMOTE_RESPONSE_ERROR);
        }
    }

    /**
     * get请求
     */
    public <T> T doGet(String url, Map<String, String> params, Class<T> clazz) {
        String pathParams = "";
        for (String key : params.keySet()) {
            if (StringUtils.equals(pathParams, "")) {
                pathParams += "?" + key + "=" + params.get(key);
            } else {
                pathParams += "&" + key + "=" + params.get(key);
            }
        }

        String strResp = "";
        try {
            CloseableHttpResponse resp = HttpUtil.get(StringConstant.API_HOST + url + pathParams);
            strResp = EntityUtils.toString(resp.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw new ServiceException(StringConstant.REMOTE_ERROR);
        }

        if (StringUtils.isNotBlank(strResp)) {
            JSONObject jsonobject = JSON.parseObject(strResp);
            return this.doresult(jsonobject, clazz);
        } else {
            throw new ServiceException(StringConstant.REMOTE_RESPONSE_ERROR);
        }
    }

    /**
     * 返回信息处理
     */
    private <T> T doresult(JSONObject jsonobject, Class<T> clazz) {
        if ((int) jsonobject.get("code") == 0) {
            T dto = JSON.toJavaObject(jsonobject.getJSONObject("content"), clazz);
            return dto;
        } else {
            throw new ServiceException(jsonobject.get("content").toString());
        }
    }
}