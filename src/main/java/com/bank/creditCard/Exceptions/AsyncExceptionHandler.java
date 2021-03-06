package com.bank.creditCard.Exceptions;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(AsyncExceptionHandler.class);
	
	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... objects) {
		logger.error("Method name : {} ", method.getName(),   ex);
		for (Object object : objects) {
			logger.error("Parameter value : {}", object);
		}
		
	}

}
