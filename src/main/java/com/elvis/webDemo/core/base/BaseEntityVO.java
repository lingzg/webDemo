package com.elvis.webDemo.core.base;

import com.elvis.webDemo.core.common.Constants;

public class BaseEntityVO {

    private int pno;
    private int size;
    private int startRow;
    
    public BaseEntityVO() {
        this.pno=1;
        this.size=Constants.PAGE_SIZE;
        this.startRow=0;
    }
    
    public int getPno() {
        return pno;
    }
    public void setPno(int pno) {
        this.pno = pno;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public int getStartRow() {
        return startRow;
    }
    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }
 
}
