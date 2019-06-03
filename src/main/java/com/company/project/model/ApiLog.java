package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "api_log")
public class ApiLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求类型
     */
    @Column(name = "http_method")
    private String httpMethod;

    /**
     * 响应类名
     */
    @Column(name = "class_method")
    private String classMethod;

    /**
     * 请求发起地址
     */
    private String ip;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 用户类型/请求来源(微信/后台)
     */
    @Column(name = "from_client")
    private String fromClient;

    /**
     * 请求发起时间
     */
    @Column(name = "request_time")
    private Date requestTime;

    /**
     * 响应时间
     */
    @Column(name = "response_time")
    private Date responseTime;

    /**
     * 请求参数
     */
    private String param;

    /**
     * 返回
     */
    private String result;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取请求地址
     *
     * @return url - 请求地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置请求地址
     *
     * @param url 请求地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取请求类型
     *
     * @return http_method - 请求类型
     */
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * 设置请求类型
     *
     * @param httpMethod 请求类型
     */
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * 获取响应类名
     *
     * @return class_method - 响应类名
     */
    public String getClassMethod() {
        return classMethod;
    }

    /**
     * 设置响应类名
     *
     * @param classMethod 响应类名
     */
    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    /**
     * 获取请求发起地址
     *
     * @return ip - 请求发起地址
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置请求发起地址
     *
     * @param ip 请求发起地址
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取用户类型/请求来源(微信/后台)
     *
     * @return from_client - 用户类型/请求来源(微信/后台)
     */
    public String getFromClient() {
        return fromClient;
    }

    /**
     * 设置用户类型/请求来源(微信/后台)
     *
     * @param fromClient 用户类型/请求来源(微信/后台)
     */
    public void setFromClient(String fromClient) {
        this.fromClient = fromClient;
    }

    /**
     * 获取请求发起时间
     *
     * @return request_time - 请求发起时间
     */
    public Date getRequestTime() {
        return requestTime;
    }

    /**
     * 设置请求发起时间
     *
     * @param requestTime 请求发起时间
     */
    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    /**
     * 获取响应时间
     *
     * @return response_time - 响应时间
     */
    public Date getResponseTime() {
        return responseTime;
    }

    /**
     * 设置响应时间
     *
     * @param responseTime 响应时间
     */
    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * 获取请求参数
     *
     * @return param - 请求参数
     */
    public String getParam() {
        return param;
    }

    /**
     * 设置请求参数
     *
     * @param param 请求参数
     */
    public void setParam(String param) {
        this.param = param;
    }

    /**
     * 获取返回
     *
     * @return result - 返回
     */
    public String getResult() {
        return result;
    }

    /**
     * 设置返回
     *
     * @param result 返回
     */
    public void setResult(String result) {
        this.result = result;
    }
}