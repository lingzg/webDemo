package com.elvis.webDemo.core.common;

import java.util.Set;

import com.alibaba.fastjson.JSON;

public class WebUser {

	private Long userId;
	private String account;
	private String nickName;
	private Set<String> permissions;
	private Set<String> roles;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Set<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	public String toString(){
		return JSON.toJSONString(this);
	}
}
