package com.company.project.configurer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import com.company.project.core.Result;
import com.company.project.core.ResultCode;
import com.company.project.core.ServiceException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

/**
 * Spring MVC 配置
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);
    @Value("${spring.profiles.active}")
    private String env;// 当前激活的配置文件

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        // 添加swagger
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    // 使用阿里 FastJson 作为JSON MessageConverter
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue, // 保留空的字段
                SerializerFeature.WriteDateUseDateFormat);
        // SerializerFeature.WriteNullStringAsEmpty,//String null -> ""
        // SerializerFeature.WriteNullNumberAsZero//Number null -> 0
        // 按需配置，更多参考FastJson文档哈
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        converters.add(converter);
    }

    // 统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                    Object handler, Exception e) {
                Result<?> result = new Result<>();
                if (e instanceof ServiceException) {// 业务失败的异常，如“账号或密码错误”
                    result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                    logger.info(e.getMessage());
                } else if (e instanceof NoHandlerFoundException) {
                    result.setCode(ResultCode.NOT_FOUND).setMessage("接口 [" + request.getRequestURI() + "] 不存在");
                } else if (e instanceof ServletException) {
                    result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                } else {
                    result.setCode(ResultCode.INTERNAL_SERVER_ERROR)
                            .setMessage("接口 [" + request.getRequestURI() + "] 内部错误: " + e.getMessage()); // !上线后去除测试输出异常
                    String message;
                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s", request.getRequestURI(),
                                handlerMethod.getBean().getClass().getName(), handlerMethod.getMethod().getName(),
                                e.getMessage());
                    } else {
                        message = e.getMessage();
                    }
                    logger.error(message, e);
                }
                responseResult(response, result);
                return new ModelAndView();
            }

        });
    }

    // 解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // registry.addMapping("/**");
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT").maxAge(3600);
    }

    // 添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 接口签名认证拦截器，该签名认证比较简单，实际项目中可以使用Json Web Token或其他更好的方式替代。
        // 使用 spring security 不再单独添加拦截器
    }

    private void responseResult(HttpServletResponse response, Result<?> result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    /**
     * 一个简单的签名认证，规则： 1. 将请求参数按ascii码排序 2. 拼接为a=value&b=value...这样的字符串（不包含sign） 3.
     * 混合密钥（secret）进行md5获得签名，与请求的签名进行比较
     */
    @SuppressWarnings("all")
    private boolean validateSign(HttpServletRequest request) {
        String requestSign = request.getParameter("sign");// 获得请求签名，如sign=19e907700db7ad91318424a97c54ed57
        if (StringUtils.isBlank(requestSign)) {
            return false;
        }
        List<String> keys = new ArrayList<String>(request.getParameterMap().keySet());
        keys.remove("sign");// 排除sign参数
        Collections.sort(keys);// 排序

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(request.getParameter(key)).append("&");// 拼接字符串
        }
        String linkString = sb.toString();
        linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);// 去除最后一个'&'

        String secret = "Potato";// 密钥，自己修改
        String sign = DigestUtils.md5Hex(linkString + secret);// 混合密钥md5

        return StringUtils.equals(sign, requestSign);// 比较
    }
    /*
     * https
     * 
     * @Bean public Connector connector() { Connector connector = new
     * Connector("org.apache.coyote.http11.Http11NioProtocol");
     * connector.setScheme("http"); connector.setPort(8080);
     * connector.setSecure(false); connector.setRedirectPort(443); return connector;
     * }
     * 
     */
    /**
     * it's for set http url auto change to https
     */
    /*
     * @Value("${Redirect443}") private Boolean Redirect443;
     * 
     * @Bean public EmbeddedServletContainerFactory servletContainer() {
     * TomcatEmbeddedServletContainerFactory tomcat = new
     * TomcatEmbeddedServletContainerFactory() {
     * 
     * @Override protected void postProcessContext(Context context) { if
     * (Redirect443) { SecurityConstraint securityConstraint = new
     * SecurityConstraint(); securityConstraint.setUserConstraint("CONFIDENTIAL");//
     * confidential SecurityCollection collection = new SecurityCollection();
     * collection.addPattern("/*"); securityConstraint.addCollection(collection);
     * context.addConstraint(securityConstraint); } } };
     * tomcat.addAdditionalTomcatConnectors(connector()); return tomcat;
     * 
     * }
     */
}
