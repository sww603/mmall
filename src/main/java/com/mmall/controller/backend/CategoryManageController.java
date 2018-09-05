package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory.Default;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.management.snmp.AdaptorBootstrap.DefaultValues;

/**
 * Created by sww_6 on 2018/7/19.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

  @Autowired
  private IUserService userService;
  @Autowired
  private ICategoryService iCategoryService;

  @RequestMapping("add_catagory.do")
  @ResponseBody
  public ServerResponse addCategory(HttpSession session, String categoryName,
      @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录！");
    }

    if (userService.checkAdminRole(user).isSuccess()) {
      return iCategoryService.addcateGory(categoryName, parentId);
    } else {
      return ServerResponse.createByErrorMessage("无权限操作，需要管理员操作！");
    }
  }

  @RequestMapping("set_catagoryName.do")
  @ResponseBody
  public ServerResponse serCategoryName(HttpSession session, Integer categoryId,
      String categoryName) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录！");
    }
    if (userService.checkAdminRole(user).isSuccess()) {
      return iCategoryService.updateCateGoryName(categoryId, categoryName);
    } else {
      return ServerResponse.createByErrorMessage("无权限操作，需要管理员操作！");
    }
  }

  @RequestMapping("get_catagory.do")
  @ResponseBody
  public ServerResponse get_catagory(HttpSession session,
      @RequestParam(value = "categoryId", defaultValue = "0") Integer parentId) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录！");
    }
    if (userService.checkAdminRole(user).isSuccess()) {
      return iCategoryService.getChildrenParallelCategory(parentId);
    } else {
      return ServerResponse.createByErrorMessage("无权限操作，需要管理员操作！");
    }
  }

  /**
   * 查询当前节点的id和递归子节点的id
   * @param session
   * @param parentId
   * @return
   */
  @RequestMapping("get_deep_catagory.do")
  @ResponseBody
  public ServerResponse get_deep_catagory(HttpSession session,
      @RequestParam(value = "categoryId", defaultValue = "0") Integer parentId) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录！");
    }
    if (userService.checkAdminRole(user).isSuccess()) {
      return iCategoryService.getChildrenParallelCategory(parentId);
    } else {
      return ServerResponse.createByErrorMessage("无权限操作，需要管理员操作！");
    }
  }
}
