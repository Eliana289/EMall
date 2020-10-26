package com.emall.service.impl;

import com.google.common.collect.Lists;
import com.emall.service.IFileService;
import com.emall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {


    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        log.info("uploading,file name:{},file dir:{},new file name:{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);


        try {
            file.transferTo(targetFile);

            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            targetFile.delete();
        } catch (IOException e) {
            log.error("file uploading error",e);
            return null;
        }
        //A:abc.jpg
        //B:abc.jpg
        return targetFile.getName();
    }

}
