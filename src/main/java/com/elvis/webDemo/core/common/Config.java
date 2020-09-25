package com.elvis.webDemo.core.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Integer.MIN_VALUE)
@Component
@PropertySource(value = "classpath:config.properties")
public class Config {
    
    @Value("${upload.fileDir}")
    public String fileDir;
   
}
