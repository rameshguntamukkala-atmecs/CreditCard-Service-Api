package com.bank.creditCard.configuration;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.bank.creditCard.exceptions.AsyncExceptionHandler;

/**
 * This is a configuration class for Async service
 *
 */
@Configuration
@EnableAsync
public class ServiceAsyncConfig implements AsyncConfigurer {
 /**
  * This method will generate a {@link ThreadPoolTaskExecutor} bean
  * @return {@link Executor} with ThreadPool configuration
  */
 @Bean(name = "asyncExecutor")
 public Executor asyncExecutor() {
  ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
  executor.setCorePoolSize(3);
  executor.setMaxPoolSize(3);
  executor.setQueueCapacity(100);
  executor.setThreadNamePrefix("Pool-");
  executor.initialize();
  return executor;
 }

 /**
  * This is a configuration method for Async exception handler
  */
 @Override
 public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
  return new AsyncExceptionHandler();
 }
}
