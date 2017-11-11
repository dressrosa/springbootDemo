/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.common.base;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.xiaoyu.common.utils.JedisUtils;

/**
 * 计算方法运行时间
 * 
 * @author xiaoyu 2016年4月3日
 */
@Configuration
@Aspect
public class ExecutionTimeAop {

    private final static Logger logger = LoggerFactory.getLogger(ExecutionTimeAop.class);

    @Pointcut("execution(* com.xiaoyu.modules.sys.controller..*(..))")
    public void pointcut() {

    }

    private static final int URI_LIMIT = 100;// 限制次数

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        final Object[] args = point.getArgs();
        HttpServletRequest request = null;
        String ip = null;// 客户端ip
        String uri = null;// 接口地址
        final String userId = null;// 用户id
        final String token = null;// 登录 token
        String ipLimit = null;//
        final String redis_userId = null;
        String methodName = null;// 调用方法名
        int limit = 0;// ip限制访问次数
        for (final Object o : args) {
            if (o instanceof HttpServletRequest) {
                request = (HttpServletRequest) o;
                break;
            }
        }
        ip = request.getRemoteHost();
        uri = request.getRequestURI();
        ExecutionTimeAop.logger.info("ip:" + ip + " uri:" + uri);
        methodName = point.getSignature().getName();
        if (uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith(".jpeg")) {
            return null;
        }
        // 无需登录情况下
        // if (uri.startsWith("/public")) {
        ipLimit = JedisUtils.hget(methodName + ":" + ip, uri);
        if (null == ipLimit) {
            JedisUtils.hset(methodName + ":" + ip, uri, 1 + "", 60);
            ipLimit = "1";
        }
        limit = Integer.valueOf(ipLimit);
        if (limit > ExecutionTimeAop.URI_LIMIT) {
            final ResponseMapper mapper = ResponseMapper.createMapper();
            mapper.code(ResponseCode.FAILED.statusCode()).message("访问次数异常,一分钟之内无法访问");
            JedisUtils.hset(methodName + ":" + ip, uri, limit + "", 60);
            JedisUtils.hincrby(methodName + ":" + ip, uri, 1);
            return mapper.resultJson();
        } else {
            JedisUtils.hincrby(methodName + ":" + ip, uri, 1);
        }
        return this.getResult(point);
        // }
        // 需登录情况下
        // userId = request.getHeader("userId");
        // token = request.getHeader("token");
        // redis_userId = JedisUtils.get(token);
        // if (null == redis_userId) {
        // ResponseMapper mapper = ResponseMapper.createMapper();
        // mapper.setCode(ResponseCode.ARGS_ERROR).setMessage("token异常,请重新登录");
        // return mapper.getResultJson();
        // }
        // if (redis_userId.equals(userId)) {
        // JedisUtils.set(token, userId, 6 * 60 * 60);
        // } else {
        // ResponseMapper mapper = ResponseMapper.createMapper();
        // mapper.setCode(ResponseCode.ARGS_ERROR).setMessage("请先登录");
        // return mapper.getResultJson();
        // }
        // // 一个用户同一个ip下访问同一个api的次数(1 min之内)
        // ipLimit = JedisUtils.hget(userId + ":" + ip, uri);
        // if (null == ipLimit) {
        // JedisUtils.hset(userId + ":" + ip, uri, 1 + "", 60);
        // ipLimit = "1";
        // }
        // limit = Integer.valueOf(ipLimit);
        // if (limit > URI_LIMIT) {
        // ResponseMapper mapper = ResponseMapper.createMapper();
        // mapper.setCode(ResponseCode.EXCEPTION).setMessage("访问次数异常,一分钟之内无法访问");
        // JedisUtils.hset(userId + ":" + ip, uri, limit + "", 60);
        // JedisUtils.hincrby(userId + ":" + ip, uri, 1);
        // return mapper.getResultJson();
        // } else {
        // JedisUtils.hincrby(userId + ":" + ip, uri, 1);
        // }
        // return getResult(point);
    }

    private Object getResult(ProceedingJoinPoint point) {
        Object result = null;
        long start = 0;
        long end = 0;
        final String methodName = point.getSignature().getName();
        try {
            start = System.currentTimeMillis();
            result = point.proceed();
            end = System.currentTimeMillis();
        } catch (final Throwable e) {
            ExecutionTimeAop.logger.error(e.toString());
        }
        ExecutionTimeAop.logger.info("方法[" + methodName + "]执行时间为:[" + (end - start) + " milliseconds] ");
        return result;
    }

}
