package com.elvis.webDemo.core.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class BaseController {

    protected final Logger log = Logger.getLogger(getClass().getName());
    protected SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected SimpleDateFormat datefmt = new SimpleDateFormat("yyyy-MM-dd");
    
	protected void write(HttpServletResponse response, String msg) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(msg);
		out.close();
	}
}
