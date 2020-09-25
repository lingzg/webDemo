package com.elvis.webDemo.core.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elvis.webDemo.core.system.model.SysUser;
import com.elvis.webDemo.core.system.model.vo.SysUserVO;

public interface SysUserDao {

    List<SysUser> findAll();
    
    List<SysUser> findPage(SysUserVO vo);
    int findPageCount(SysUserVO vo);
    
    SysUser getById(@Param("userId")Long userId);
    
    void save();
    
    void update();
    
    void delete();
}
