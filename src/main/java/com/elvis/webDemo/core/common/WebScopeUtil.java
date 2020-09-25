package com.elvis.webDemo.core.common;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/***
 * 作用域
 * 
 * @author win7 宋祥
 */
public class WebScopeUtil {

	/****
	 * 获取Request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		if (ra != null) {
			ServletRequestAttributes sa = (ServletRequestAttributes) ra;
			HttpServletRequest rq = sa.getRequest();
			if (rq != null) {
				return rq;
			}
		}
		return null;
	}

	/***
	 * 获取session
	 * 
	 * @return
	 */
	public static HttpSession getSession() {
		HttpServletRequest req = getRequest();
		if (req != null) {
			// 如果获取不到 则帮助创建session
			return req.getSession();
		}
		return null;
	}

	/***
	 * 获取ServletContext
	 * 
	 * @return
	 */
	public static ServletContext getApplication() {
		HttpSession session = getSession();
		if (session != null) {
			return session.getServletContext();
		}
		return null;
	}
	
	public static String getParam(){
		HttpServletRequest req = getRequest();
		if (req != null) {
			return getParam(req);
		}
		return null;
	}
	
	public static String getParam(HttpServletRequest request){
		JSONObject json = new JSONObject();
		Enumeration<?> enu = request.getParameterNames();
		while(enu.hasMoreElements()){
			String k = (String) enu.nextElement();
			String[] v = request.getParameterValues(k);
			if(v!=null&&v.length>0){
				json.put(k, v.length>1?v:v[0]);
			}
		}
		return json.toString();
	}
}
