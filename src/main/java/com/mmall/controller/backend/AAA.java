package com.mmall.controller.backend;

import com.mmall.pojo.Ftp;
import java.io.File;
/**
 * Created by sww_6 on 2018/9/29.
 */
public class AAA {

  public static void main(String[] args) {
    Ftp f = new Ftp();
    f.setIpAddr("39.106.140.104");
    f.setUserName("test");
    f.setPwd("test");
    f.setPath("/home/test/sww/");
    f.setPort(21);
    try {
      //FtpUtils.connectFtp(f);
    } catch (Exception e) {
      e.printStackTrace();
    }
    File file = new File("D:/images/774f52b29bc191f5b916d9405b7b0904_1.jpg");
    try {
      //com.sww.FtpUtils.upload(file);//把文件上传在ftp上
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("上传文件完成。。。。");
  }
}
