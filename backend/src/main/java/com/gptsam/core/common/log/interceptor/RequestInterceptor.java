package com.gptsam.core.common.log.interceptor;

import com.gptsam.core.common.log.logger.RequestTimeIntervalLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {

	private final RequestTimeIntervalLogger requestTimeIntervalLogger;


	@Override
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response,
							 Object handler) {
		requestTimeIntervalLogger.start();
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
								HttpServletResponse response,
								Object handler,
								Exception ex) {
		logRequestTime(request);
	}

	private void logRequestTime(HttpServletRequest request) {
		String methodName = request.getMethod();
		String requestUri = request.getRequestURI();
		requestTimeIntervalLogger.log(methodName, requestUri);
	}

}
