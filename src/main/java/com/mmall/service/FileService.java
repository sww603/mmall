package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by sww_6 on 2018/9/8.
 */
public interface FileService {

  String upload(MultipartFile file,String path);
}
