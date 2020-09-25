package com.elvis.webDemo.core.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elvis.webDemo.core.base.BaseService;
import com.elvis.webDemo.core.common.PageInfo;
import com.elvis.webDemo.core.system.dao.SysUserDao;
import com.elvis.webDemo.core.system.model.SysUser;
import com.elvis.webDemo.core.system.model.vo.SysUserVO;

@Service
@Transactional
public class SysUserService extends BaseService{

    @Autowired
    private SysUserDao userDao;
    
    public List<SysUser> findList(){
        return userDao.findAll();
    }
    
    public PageInfo findPage(SysUserVO vo) {
        PageInfo page = new PageInfo(vo);
        List<SysUser> rows = userDao.findPage(vo);
        int total = userDao.findPageCount(vo);
        page.setRows(rows);
        page.setTotal(total);
        return page;
    }
}
