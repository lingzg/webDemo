package com.elvis.webDemo.core.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.elvis.webDemo.core.base.BaseController;
import com.elvis.webDemo.core.common.JsonResult;
import com.elvis.webDemo.core.system.model.SysFile;
import com.elvis.webDemo.core.system.service.FileService;

/**
 * @since 2020/08/25
 * @author lingzg
 * 文件管理
 */
@Controller
@RequestMapping("/file")
public class FileController extends BaseController{

    @Autowired
    private FileService fileService;
    
    @ResponseBody
    @RequestMapping("/upload")
    public JsonResult upload(@RequestParam("file") MultipartFile mfile,HttpServletRequest request, HttpServletResponse response){
        JsonResult res = new JsonResult();
        try {
            if (mfile != null && !mfile.isEmpty()) {
                SysFile sysfile = fileService.upload(mfile);
                res.setStatus(1);
                res.setMsg("success");
                res.setData(sysfile);
            }else{
                log.error("no file recived");
                res.setStatus(0);
                res.setMsg("no file recived");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setStatus(0);
            res.setMsg(e.getMessage());
        }
        return res;
    }
    
    @RequestMapping("/preview")
    public String preview(HttpServletRequest request, HttpServletResponse response){
    	return "/preview";
//    	return "redirect:http://127.0.0.1:8082/web/static/beb44bc483fd6c9ef12d42c82aefcd7b.mp4";
//    	return "redirect:http://127.0.0.1:8082/web/static/test.avi";
    }
}
