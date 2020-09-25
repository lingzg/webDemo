package com.elvis.webDemo.core.interceptor;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.elvis.webDemo.core.common.WebScopeUtil;

/**
 * 日志拦截器
 */
public class LogInterceptor implements HandlerInterceptor {
	
	private final Logger log = Logger.getLogger(getClass().getName());
	private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		long beginTime = System.currentTimeMillis(); // 开始时间
		log.info("开始时间:" + fmt.format(beginTime) + ":" + request.getRequestURI() + ",请求方式:" + request.getMethod());
		log.info("请求数据:" + WebScopeUtil.getParam(request));
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			log.info("ViewName: " + modelAndView.getViewName());
		}
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long endTime = System.currentTimeMillis(); // 结束时间
		log.info("结束时间:" + fmt.format(endTime));
	}

}