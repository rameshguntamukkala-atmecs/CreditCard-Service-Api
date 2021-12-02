package com.bank.creditCard.configuration;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.bank.creditCard.Exceptions.AsyncExceptionHandler;

@Configuration
@EnableAsync
public class ServiceAsyncConfig implements AsyncConfigurer{

	 	@Bean(name = "asyncExecutor")
	    public Executor asyncExecutor() 
	    {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(3);
	        executor.setMaxPoolSize(3);
	        executor.setQueueCapacity(100);
	        executor.setThreadNamePrefix("Pool-");
	        executor.initialize();
	        return executor;
	    }
	
	 	@Override
	 	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
	 		return new AsyncExceptionHandler();
	 	}
}
