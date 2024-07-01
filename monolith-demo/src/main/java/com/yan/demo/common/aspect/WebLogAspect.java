package com.yan.demo.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: sixcolor
 * @Date: 2024-04-18 19:31
 * @Description:
 */
@Component
@Aspect
public class WebLogAspect {
    private final static Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("within(com.*..*.*Controller+)")
    public void controllerLog() {
    }

    @Before("controllerLog()")
    public void beforeLog(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Optional.ofNullable(attributes).map(ServletRequestAttributes::getRequest).orElse(null);

        if (request != null) {
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = request.getRemoteAddr();
            }
            log.info("请求IP: {}", ipAddress);
            log.info("请求URL: {}", request.getRequestURL());
            log.info("HTTP 方法: {}", request.getMethod());
            log.info("类 方法: {}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            log.info("请求 参数: {}", joinPoint.getArgs());

            request.setAttribute("methodSignature", joinPoint.getSignature().toShortString());
            request.setAttribute("methodArgs", Arrays.toString(joinPoint.getArgs()));
            request.setAttribute("start", System.currentTimeMillis());
        }
    }

    @AfterReturning(pointcut = "controllerLog()", returning = "result")
    public void afterReturningLog(Object result) {
        HttpServletRequest request = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest).orElse(null);

        if (request != null) {
            long start = Optional.ofNullable((Long) request.getAttribute("start")).orElse(0L);
            long end = System.currentTimeMillis();
            log.info("请求 结果: {}", result);
            log.info("本次共耗时: {} ms", end - start);
        }
    }
}
