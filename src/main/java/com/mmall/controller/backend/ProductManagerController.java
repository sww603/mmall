package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.FileService;
import com.mmall.service.IUserService;
import com.mmall.service.ProductService;
import com.mmall.util.FtpUtils;
import com.mmall.util.PropertiesUtil;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by sww_6 on 2018/9/6.
 */
@Controller
@RequestMapping("/manage/product")
@Slf4j
public class ProductManagerController {

  @Autowired
  private IUserService iUserService;
  @Autowired
  private ProductService productService;
  @Autowired
  private FileService fileService;
  @Autowired
  private UserMapper userMapper;

  @RequestMapping("save.do")
  @ResponseBody
  public ServerResponse productSave(HttpSession session, Product product) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录管理员！");
    }
    if (iUserService.checkAdminRole(user).isSuccess()) {
      return productService.saveOrUpdateProduct(product);
    } else {
      return ServerResponse.createByErrorMessage("无权限操作！");
    }
  }

  @RequestMapping("set_sale_status.do")
  @ResponseBody
  public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录管理员！");
    }
    if (iUserService.checkAdminRole(user).isSuccess()) {
      return productService.setSaleStatus(productId, status);
    } else {
      return ServerResponse.createByErrorMessage("无权限操作！");
    }
  }

  /**
   * 获取商品详情
   */
  @RequestMapping("detail.do")
  @ResponseBody
  public ServerResponse detsil(HttpSession session, Integer productId) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录管理员！");
    }
    if (iUserService.checkAdminRole(user).isSuccess()) {
      return productService.managerProductDetails(productId);
    } else {
      return ServerResponse.createByErrorMessage("无权限操作！");
    }
  }

  /**
   * 获取商品列表
   */
  @RequestMapping("list.do")
  @ResponseBody
  public ServerResponse productList(HttpSession session,
      @RequestParam(value = "pageSize", defaultValue = "1") int pageSize,
      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录管理员！");
    }
    if (iUserService.checkAdminRole(user).isSuccess()) {
      return productService.getProductList(pageSize, pageNum);
    } else {
      return ServerResponse.createByErrorMessage("无权限操作！");
    }
  }

  /**
   * 搜索商品信息
   */
  @RequestMapping("search.do")
  @ResponseBody
  public ServerResponse searchProduct(HttpSession session, String productName,
      Integer productId, @RequestParam(value = "pageSize", defaultValue = "1") int pageSize,
      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录管理员！");
    }
    if (iUserService.checkAdminRole(user).isSuccess()) {
      return productService.searchProduct(productName, productId, pageSize, pageNum);
    } else {
      return ServerResponse.createByErrorMessage("无权限操作！");
    }
  }

  @RequestMapping("upload.do")
  @ResponseBody
  public Map upload(HttpSession session,
      @RequestParam(value = "upload_file", required = false) MultipartFile file,
      HttpServletRequest request, HttpServletResponse response) {
    Map resultMap = Maps.newHashMap();
    log.info(resultMap.toString());
    try {
      FtpUtils.connectFtp();
    } catch (Exception e) {
      e.printStackTrace();
    }
    String path = request.getSession().getServletContext().getRealPath("upload");

    String targetFileName = fileService.upload(file, path);
    if (StringUtils.isBlank(targetFileName)) {
      resultMap.put("success", false);
      resultMap.put("msg", "上传失败");
      return resultMap;
    }
    String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
    resultMap.put("success", true);
    resultMap.put("msg", "上传成功");
    resultMap.put("file_path", url);
    response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
    System.out.println("上传文件完成。。。。");
    return resultMap;
  }

  @RequestMapping("richtext_img_upload.do")
  @ResponseBody
  public Map richtextImgUpload(HttpSession session,
      @RequestParam(value = "upload_files", required = false) MultipartFile files,
      HttpServletRequest request, HttpServletResponse response) {
    Map resultMap = Maps.newHashMap();
    log.info(resultMap.toString());
    try {
      FtpUtils.connectFtp();
    } catch (Exception e) {
      e.printStackTrace();
    }
    String path = request.getSession().getServletContext().getRealPath("upload");

    String targetFileName = fileService.upload(files, path);
    if (StringUtils.isBlank(targetFileName)) {
      resultMap.put("success", false);
      resultMap.put("msg", "上传失败");
      return resultMap;
    }
    String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
    resultMap.put("success", true);
    resultMap.put("msg", "上传成功");
    resultMap.put("file_path", url);
    response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
    System.out.println("上传文件完成。。。。");
    return resultMap;
  }
}
