package com.elvis.webDemo.core.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.elvis.webDemo.core.annotation.Authorization;
import com.elvis.webDemo.core.common.WebUser;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
	private final Logger log = Logger.getLogger(getClass().getName());
	
	public static final int NO_PERMISSION = 301;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,content-type,token");
		response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
		// 如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		Authorization auth;
		if ((auth = method.getAnnotation(Authorization.class)) == null) {
			return true;
		}
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");
		boolean perm = true;
		if (obj != null) {
			WebUser user = (WebUser) obj;
			if(checkPermission(user, auth)){
				return true;
			}else{
				perm = false;
			}
		}
		String requestType = request.getHeader("X-Requested-With");
		if ("XMLHttpRequest".equalsIgnoreCase(requestType)) {
			response.addHeader("loginStatus", "accessDenied");
			response.sendError(perm ? HttpServletResponse.SC_UNAUTHORIZED : NO_PERMISSION);
		}else{
			response.sendRedirect(request.getContextPath()+(perm ? "/login.jsp" : "/noperm.jsp"));
		}
		return false;
	}

	private boolean checkPermission(WebUser user, Authorization auth) {
		String perm = auth.requiredPermissions();
		Set<String> permissions = user.getPermissions();
		if(!StringUtils.isBlank(perm)){
			boolean flag = Arrays.stream(perm.split("\\|")).anyMatch(x -> 
				Arrays.stream(x.split("&")).allMatch(y -> permissions.contains(y) || permissions.contains(matchAll(y)))
			);
			if(!flag){
				return false;
			}
		}
		String role = auth.requiredRoles();
		Set<String> roles = user.getRoles();
		if(!StringUtils.isBlank(role)){
			boolean flag = Arrays.stream(role.split("\\|")).anyMatch(x -> 
				Arrays.stream(x.split("&")).allMatch(y -> roles.contains(y))
			);
			if(!flag){
				return false;
			}
		}
		String usr = auth.requiredUsers();
		if(!StringUtils.isBlank(usr)){
			boolean flag = Arrays.stream(usr.split("\\|")).anyMatch(x -> x.equals(user.getAccount()));
			if(!flag){
				return false;
			}
		}
		return true;
	}
	
	private String matchAll(String perm){
		if(perm.indexOf(":")>0){
			return perm.split(":")[0]+"*";
		}
		return perm;
	}

}