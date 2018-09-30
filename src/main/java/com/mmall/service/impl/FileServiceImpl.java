package com.mmall.service.impl;

import com.mmall.service.FileService;
import com.mmall.util.FtpUtils;
import java.io.File;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by sww_6 on 2018/9/8.
 */
@Slf4j
@Service("fileService")
public class FileServiceImpl implements FileService {

  @Override
  public String upload(MultipartFile file, String path) {
    String fileName = file.getOriginalFilename();
    //扩展名
    //abc.jpg
    String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
    String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
    log.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);

    File fileDir = new File(path);
    if(!fileDir.exists()){
      fileDir.setWritable(true);
      fileDir.mkdirs();
    }
    File targetFile = new File(path,uploadFileName);

    try {
      file.transferTo(targetFile);

      FtpUtils.upload(targetFile);//把文件上传在ftp上
    } catch (Exception e) {
      e.printStackTrace();
    }
    return targetFile.getName();

  }
}
