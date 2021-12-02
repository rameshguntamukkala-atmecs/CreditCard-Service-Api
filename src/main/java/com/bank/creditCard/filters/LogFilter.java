package com.bank.creditCard.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogFilter implements Filter{

	private static Logger logger = LoggerFactory.getLogger(LogFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		long systemTime = System.currentTimeMillis();
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		
		logger.info("RequestURI - {}", servletRequest.getRequestURI());
		logger.info("MethodType - {}", servletRequest.getMethod());
		
		chain.doFilter(request, response);

		logger.info("Total Time: {} milliseconds", (System.currentTimeMillis() - systemTime));
		
		
	}

}
