package cn.meyoung.wlfz.webapi.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * AOP拦截器接口
 */
public interface IBaseAspect {

    void doBefore(JoinPoint jp);

    Object doAround(ProceedingJoinPoint pjp) throws Throwable;

    void doAfter(JoinPoint jp);

    void doThrowing(JoinPoint jp, Throwable ex);
}
