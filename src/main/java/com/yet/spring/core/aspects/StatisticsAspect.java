package com.yet.spring.core.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Aspect
public class StatisticsAspect {

    private Map<Class<?>, Integer> counter = new HashMap<>();

    @Pointcut("execution(* com.yet.spring.core.loggers.*.logEvent(..))")
    private void allLogEventMethods() {}

    @AfterReturning("allLogEventMethods()")
    public void count(JoinPoint jp) {
        Class<?> clazz = jp.getTarget().getClass();
        if (!counter.containsKey(clazz)) {
            counter.put(clazz, 0);
        }
        counter.put(clazz, counter.get(clazz) + 1);
    }

    public Map<Class<?>, Integer> getCounter() {
        return Collections.unmodifiableMap(counter);
    }

    @AfterReturning("execution(* com.yet.spring.core.util.App.logEvents(..))")
    public void outputLoggingCounter() {
        System.out.println("Loggers statistics. Number of calls: ");
        for (Entry<Class<?>, Integer> entry : counter.entrySet()) {
            System.out.println("    " + entry.getKey().getSimpleName() + ": " + entry.getValue());
        }
    }

}
