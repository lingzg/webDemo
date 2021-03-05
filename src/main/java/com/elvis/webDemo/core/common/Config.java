package com.elvis.webDemo.core.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    
    @Value("${upload.fileDir}")
    public String fileDir;
   
}
