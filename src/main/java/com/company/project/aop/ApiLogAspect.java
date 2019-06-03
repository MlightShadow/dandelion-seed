package com.company.project.aop;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.company.project.dto.AuthorizedInfo;
import com.company.project.model.ApiLog;
import com.company.project.service.ApiLogService;
import com.company.project.util.SecurityContextUtil;
import com.company.project.util.UUIDUtil;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ApiLogAspect {
    @Autowired
    private SecurityContextUtil securityContextUtil;

    @Autowired
    private UUIDUtil uuid;

    @Resource
    private ApiLogService apiLogService;

    ThreadLocal<String> request_id = new ThreadLocal<>();

    @Pointcut("@annotation(com.company.project.aop.NeedApiLog)")
    public void apiLog() {
    }

    @Before("apiLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        AuthorizedInfo authInfo = securityContextUtil.getAuthInfo();

        request_id.set(uuid.getUUID());
        ApiLog log = new ApiLog();
        log.setId(request_id.get());
        log.setUrl(request.getRequestURI().toString());
        log.setHttpMethod(request.getMethod());
        log.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.setIp(getIpAddress(request));
        if (authInfo != null) {
            log.setUserId(authInfo.getId());
            log.setFromClient("backend");
        }
        log.setRequestTime(new Date());

        log.setParam(JSONArray.toJSONString(joinPoint.getArgs()));

        apiLogService.save(log);
    }

    @AfterReturning(returning = "ret", pointcut = "apiLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        ApiLog log = new ApiLog();
        log.setId(request_id.get());
        log.setResponseTime(new Date());
        log.setResult(ret.toString());

        apiLogService.update(log);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return ip;
    }
}