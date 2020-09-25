package com.elvis.webDemo.core.util;

import java.util.UUID;

public class UuidUtil {

    public static String getUuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    public static String getUuid(String name){
        return UUID.fromString(name).toString().replace("-", "");
    }
}
