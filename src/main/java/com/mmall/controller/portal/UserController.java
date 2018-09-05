package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by sww_6 on 2018/7/4.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

  @Autowired
  private IUserService iUserService;

  /**
   * 用户登录
   */
  @RequestMapping(value = "login.do", method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<User> login(String username, String password, HttpSession httpSession) {
    ServerResponse<User> response = iUserService.login(username, password);
    if (response.isSuccess()) {
      httpSession.setAttribute(Const.CURRENT_USER, response.getData());
    }
    return response;
  }

  @RequestMapping(value = "logout.do", method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<String> logout(HttpSession session) {
    session.removeAttribute(Const.CURRENT_USER);
    return ServerResponse.createBySuccess();
  }

  @RequestMapping(value = "register.do", method = RequestMethod.POST)
  @ResponseBody
  public ServerResponse<String> register(User user) {
    return iUserService.register(user);
  }

  @RequestMapping(value = "check_valid.do")
  @ResponseBody
  public ServerResponse<String> checkValid(String str, String type) {
    return iUserService.checkValid(str, type);
  }

  /*@RequestMapping(value = "get_user_info.do")
  @ResponseBody
  public ServerResponse<User> getUserInfo(HttpSession session) {
    User attribute = (User) session.getAttribute(Const.CURRENT_USER);
    if (attribute != null) {
      return ServerResponse.createBySuccess(attribute);
    }
    return ServerResponse.createByErrorMessage("用户未登录，无法获取用户当前信息！");
  }*/

  @RequestMapping(value = "get_user_info.do")
  @ResponseBody
  public ServerResponse<String> forgetGetQuestion(String userName) {
    ServerResponse<String> stringServerResponse = iUserService.selectQuestion(userName);
    return stringServerResponse;
  }


  @RequestMapping(value = "reset_password.do")
  @ResponseBody
  public ServerResponse<String> resetPassword(HttpSession session, String passwordOld,
      String passwordNew) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      ServerResponse.createByErrorMessage("用户未登录！");
    }
    return iUserService.resetPassword(passwordOld, passwordNew, user);
  }

  @RequestMapping(value = "update_information.do")
  @ResponseBody
  public ServerResponse<User> update_information(HttpSession session, User user) {
    User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
    if (currentUser == null) {
      ServerResponse.createByErrorMessage("该用户未登录");
    }
    user.setId(currentUser.getId());
    user.setUsername(currentUser.getUsername());
    ServerResponse<User> response = iUserService.updateInformation(user);
    if (response.isSuccess()) {
      response.getData().setUsername(currentUser.getUsername());
      session.setAttribute(Const.CURRENT_USER, response.getData());
    }
    return response;
  }

 /* @RequestMapping(value = "get_information.do")
  @ResponseBody
  public ServerResponse<User> get_information(HttpSession session) {
    User attribute = (User) session.getAttribute(Const.CURRENT_USER);
    if (attribute == null) {
      return ServerResponse
          .createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
    }
    return iUserService.getInformation(attribute.getId());
  }*/
}

