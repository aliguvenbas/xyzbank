package com.ag.xyzbank.config;

import java.util.concurrent.atomic.AtomicLong;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class QueryRateLimiter {

	private static final long RATE_LIMIT_INTERVAL_MS = 1000; // 1 second
	private static final int MAX_QUERIES_PER_SECOND = 2; // Adjust this value according to your requirement

	private final AtomicLong lastInvocationTime = new AtomicLong(0);
	private final AtomicLong queryCount = new AtomicLong(0);

	@Before(value = "execution(* com.ag.xyzbank.repository.*.*(..))")
	public void limitQueryRate(JoinPoint joinPoint) throws Exception {
		long currentTime = System.currentTimeMillis();
		long lastTime = lastInvocationTime.get();
		if (currentTime - lastTime > RATE_LIMIT_INTERVAL_MS) {
			queryCount.set(0);
			lastInvocationTime.set(currentTime);
		}
		if (queryCount.incrementAndGet() > MAX_QUERIES_PER_SECOND) {
			throw new RuntimeException("Query rate limit exceeded. Limit is "+MAX_QUERIES_PER_SECOND+" request per second");
		}
	}
}
