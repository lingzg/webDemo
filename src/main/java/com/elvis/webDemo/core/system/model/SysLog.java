package com.elvis.webDemo.core.system.model;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.elvis.webDemo.core.base.BaseEntity;

public class SysLog extends BaseEntity {

	private Long id;
	 
	/*"操作用户"*/	
	private String username;
 
	/*"描述"*/
	private String operation;
 
	/*"耗时（毫秒）"*/
	private Long time;
 
	/*"操作方法"*/
	private String method;
 
	/*"参数"*/
	private String params;
 
	/*"IP地址"*/
	private String ip;
	
	private String macAddress;
 
	/*"操作时间"*/
	private Date createTime;
 
	/*"地点"*/
	private String location;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String toString(){
		return JSON.toJSONString(this);
	}
}
