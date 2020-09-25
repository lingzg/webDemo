package com.elvis.webDemo.core.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.elvis.webDemo.core.base.BaseEntityVO;

public class PageInfo {

    /**
     * 页面每页记录条数
     */
    private int pageSize;

    /**
     * 总记录条数
     */
    private long total;

    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 总页数
     */
    private int pageTotal;

    private int startRow;

    private List<?> rows;

    private String order;

    private String orderFlag;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageTotal() {
        this.pageTotal = (int) (this.total%this.pageSize==0 ? this.total/this.pageSize : this.total/this.pageSize+1);
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderFlag() {
        return orderFlag;
    }

    public void setOrderFlag(String orderFlag) {
        this.orderFlag = orderFlag;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public PageInfo() {
        this.total = 0;
        this.currentPage = 1;
        this.pageSize = Constants.PAGE_SIZE;
        this.pageTotal = 0;
        this.rows = new ArrayList<Object>();
    }

    public PageInfo(BaseEntityVO vo) {
        this();
        this.currentPage = vo.getPno();
        this.pageSize = vo.getSize();
        vo.setStartRow(getStartRow());
    }

    public int getStartRow() {
        this.startRow =  (this.currentPage - 1) * this.pageSize;
        return this.startRow;
    }
    
    public String toString(){
        return JSON.toJSONString(this);
    }

}
