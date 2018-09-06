package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.service.ProductService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by sww_6 on 2018/9/6.
 */
@Controller
@RequestMapping("/manager/product")
public class ProductManagerController {

  @Autowired
  private IUserService iUserService;
  @Autowired
  private ProductService productService;

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
}
