package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.FileService;
import com.mmall.util.FTPUtil;
import java.io.File;
import java.io.IOException;
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
    String filename = file.getOriginalFilename();
    String fileExetFileName = filename.substring(filename.lastIndexOf(".") + 1);
    String uploadFileName = UUID.randomUUID().toString() + "." + fileExetFileName;
    log.info("开始上传文件，上传文件的文件名是{},上传文件的路径是{},新文件名是{}", filename, path, uploadFileName);

    File fileDir = new File(path);
    if (!fileDir.exists()) {
      fileDir.setWritable(true);
      fileDir.mkdirs();
    }
    File targetFile = new File(uploadFileName, path);
    try {
      file.transferTo(targetFile);
      // TODO: 将targetFile上传到我们的ftp服务器
      FTPUtil.uploadFile(Lists.<File>newArrayList(targetFile));
      //已经上传到ftp服务器
      // TODO: 上传完之后，删除upload下面的文件
      targetFile.delete();
    } catch (IOException e) {
      log.error("上传文件异常");
      return null;
    }
    return targetFile.getName();
  }

  public static void main(String[] args) {
    String filename = "abc.jpg.qnqn";
    System.out.println(
        UUID.randomUUID().toString() + "." + filename.substring(filename.lastIndexOf(".") + 1));
  }
}
