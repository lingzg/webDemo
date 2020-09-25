package com.elvis.webDemo.core.common;

import com.alibaba.fastjson.JSON;

public class JsonResult {

    private int status;
    private String msg;
    private Object data;
    
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
    
}
