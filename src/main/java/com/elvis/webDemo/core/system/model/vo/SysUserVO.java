package com.elvis.webDemo.core.system.model.vo;

import com.alibaba.fastjson.JSON;
import com.elvis.webDemo.core.base.BaseEntityVO;

public class SysUserVO extends BaseEntityVO {

    private Long userId;
    private String userName;
    private String password;
    private String nickName;
    private String realName;
    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String toString(){
        return JSON.toJSONString(this);
    }
}
