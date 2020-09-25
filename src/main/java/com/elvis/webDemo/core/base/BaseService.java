package com.elvis.webDemo.core.base;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.elvis.webDemo.core.common.Config;

public class BaseService {

    protected final Logger log = Logger.getLogger(getClass().getName());
    
    @Autowired
    protected BaseDao sqlDao;
    @Autowired
    protected Config config;
    
}
