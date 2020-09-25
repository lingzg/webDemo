package com.elvis.webDemo.core.system.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.elvis.webDemo.core.base.BaseService;
import com.elvis.webDemo.core.system.model.SysFile;
import com.elvis.webDemo.core.util.UuidUtil;

@Transactional
@Service
public class FileService extends BaseService{

    public SysFile upload(MultipartFile mfile){
        SysFile sysfile = new SysFile();
        String fileName = mfile.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String path = config.fileDir+"/"+fmt.format(new Date());
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String filePath = path+"/"+UuidUtil.getUuid()+"."+fileType;
        File file = new File(filePath);
        try (
            InputStream in = mfile.getInputStream();
            FileOutputStream output = new FileOutputStream(file);
        ){
            IOUtils.copy(in, output);
            sysfile.setFileName(fileName);
            sysfile.setFileType(fileType);
            sysfile.setFilePath(filePath);
            sysfile.setFileSize(mfile.getSize());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return sysfile;
    }
}
