package com.mmall.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Created by sww_6 on 2018/9/9.
 */
@Data
@Slf4j
public class FTPUtil {

  private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
  private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
  private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

  public FTPUtil(String ip, int port, String user, String pwd) {
    this.ip = ip;
    this.port = port;
    this.user = user;
    this.pwd = pwd;
  }

  public static boolean uploadFile(List<File> fileList) {
    FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
    log.info("开始连接ftp服务器");
    boolean result = ftpUtil.uploadFile("img", fileList);
    log.info("开始连接ftp服务器，结束上传，上传结束！{}");
    return result;
  }

  private boolean uploadFile(String remotePath, List<File> fileList) {
    boolean uploaded = true;
    FileInputStream fis = null;
    //连接ftp服务器
    if (connectServer(this.ip, this.port, this.user, this.pwd)) {
      //需不需要切换文件夹
      try {
        ftpClient.changeWorkingDirectory(remotePath);
        ftpClient.setBufferSize(1024);
        ftpClient.setControlEncoding("UTF-8");
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftpClient.enterLocalActiveMode();
        for (File fileItem : fileList) {
          fis = new FileInputStream(fileItem);
          ftpClient.storeFile(fileItem.getName(), fis);
        }
      } catch (IOException e) {
        e.printStackTrace();
        log.error("上传文件异常！", e);
        uploaded = false;
      } finally {
        try {
          fis.close();
          ftpClient.disconnect();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return uploaded;
  }

  private boolean connectServer(String ip, int port, String user, String pwd) {
    boolean isSuccess = false;
    ftpClient = new FTPClient();
    try {
      ftpClient.connect(ip, port);
      isSuccess = ftpClient.login(user, pwd);
    } catch (IOException e) {
      e.printStackTrace();
      log.error("连接ftp服务器异常！");
    }
    return isSuccess;
  }

  private String ip;
  private int port;
  private String user;
  private String pwd;
  private FTPClient ftpClient;

}
