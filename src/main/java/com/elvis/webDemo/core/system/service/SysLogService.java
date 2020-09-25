package com.elvis.webDemo.core.system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elvis.webDemo.core.base.BaseService;
import com.elvis.webDemo.core.system.model.SysLog;

@Service
@Transactional
public class SysLogService extends BaseService {

	public void save(SysLog log){
		this.log.info(log);
	}
}
