package com.peplatform.portfolioservice.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * Records service-operation throughput, failures and latency without coupling
 * business services to Micrometer APIs.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class PortfolioMetricsAspect {

    private final MeterRegistry meterRegistry;

    @Around("execution(public * com.peplatform.portfolioservice.service.impl..*(..))")
    public Object recordServiceOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String service = signature.getDeclaringType().getSimpleName();
        String operation = signature.getName();
        Timer.Sample sample = Timer.start(meterRegistry);
        String outcome = "success";

        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            outcome = "failure";
            meterRegistry.counter(
                    "portfolio.service.errors",
                    "service", service,
                    "operation", operation,
                    "exception", throwable.getClass().getSimpleName()
            ).increment();
            throw throwable;
        } finally {
            meterRegistry.counter(
                    "portfolio.service.operations",
                    "service", service,
                    "operation", operation,
                    "outcome", outcome
            ).increment();

            sample.stop(Timer.builder("portfolio.service.operation.duration")
                    .description("Portfolio business service operation duration")
                    .tag("service", service)
                    .tag("operation", operation)
                    .tag("outcome", outcome)
                    .register(meterRegistry));
        }
    }
}
