package com.elvis.webDemo.core.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.elvis.webDemo.core.annotation.Log;
import com.elvis.webDemo.core.base.BaseController;
import com.elvis.webDemo.core.common.PageInfo;
import com.elvis.webDemo.core.system.model.vo.SysUserVO;
import com.elvis.webDemo.core.system.service.SysUserService;

/**
 * @since 2020/08/21
 * @author lingzg
 * 用户管理
 */
@Controller
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService userService;
    
//    @Authorization
    @RequestMapping("/page")
    public String page(HttpServletRequest request, HttpServletResponse response, ModelMap model, SysUserVO vo){
        PageInfo page = userService.findPage(vo);
        log.info(page.toString());
        model.addAttribute("page", page);
        return "/system/user";
    }
    
//    @Authorization
    @Log("用户列表")
    @RequestMapping("/list")
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model, SysUserVO vo){
        PageInfo page = userService.findPage(vo);
        log.info(page.toString());
        model.addAttribute("page", page);
        return "/system/table";
    }
    
}
