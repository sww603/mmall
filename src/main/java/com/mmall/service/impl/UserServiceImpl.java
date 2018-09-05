package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.Const.Role;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sww_6 on 2018/7/4.
 */
@Service
public class UserServiceImpl implements IUserService {

  @Autowired
  private UserMapper userMapper;

  @Override
  public ServerResponse<User> login(String username, String password) {

    int i = userMapper.checkUsername(username);
    if (i == 0) {
      return ServerResponse.createByErrorMessage("用户名不存在！");
    }
    String md5Password = MD5Util.MD5EncodeUtf8(password);
    User user = userMapper.selectLogin(username, md5Password);
    if (user == null) {
      return ServerResponse.createByErrorMessage("密码错误");
    }
    return null;
  }

  @Override
  public ServerResponse<String> register(User user) {
        /*ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }*/
    user.setRole(Const.Role.ROLE_CUSTOMER);
    //MD5加密
    user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
    int resultCount = userMapper.insert(user);
    if (resultCount == 0) {
      return ServerResponse.createByErrorMessage("注册失败");
    }
    return ServerResponse.createBySuccessMessage("注册成功");
  }

  @Override
  public ServerResponse<String> checkValid(String str, String type) {
    if (StringUtils.isNotEmpty(type)) {
      //开始校验
      if (Const.USERNAME.equals(type)) {
        int i = userMapper.checkUsername(type);
        if (i > 0) {
          ServerResponse.createByErrorMessage("用户名易经经存在！");
        }
      }
      if (Const.EMAIL.equals(type)) {
        int i = userMapper.checkEmail(type);
        if (i > 0) {
          ServerResponse.createByErrorMessage("email已经存在！");
        }
      }
    } else {
      return ServerResponse.createByErrorMessage("参数错误！");
    }
    return null;
  }

  @Override
  public ServerResponse<String> selectQuestion(String userName) {
    ServerResponse<String> stringServerResponse = this.checkValid(userName, Const.USERNAME);
    if (stringServerResponse.isSuccess()) {
      return ServerResponse.createByErrorMessage("用户名不存在！");
    }
    String s = userMapper.selectQueationByUsername(userName);
    if (StringUtils.isNotEmpty(s)) {
      return ServerResponse.createBySuccess(s);
    }
    return ServerResponse.createByErrorMessage("找回密码的问题是空的！");
  }

  @Override
  public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {

    //判断密码是否正确
    int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8("passwordOld"), user.getId());
    if (resultCount == 0) {
      ServerResponse.createByErrorMessage("旧密码错误");
    }
    //更新用户密码
    user.setPassword(passwordNew);//要测试一下，这里是否可以直接设置值
    int i = userMapper.updateByPrimaryKeySelective(user);
    if (i > 0) {
      return ServerResponse.createBySuccessMessage("密码更新成功");
    }
    return ServerResponse.createByErrorMessage("密码更新失败");
  }

  @Override
  public ServerResponse<User> updateInformation(User user) {
    int i = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
    if (i > 0) {
      ServerResponse.createByErrorMessage("email已存在,请更换email再尝试更新");
    }

    User updateUser = new User();
    updateUser.setId(user.getId());
    updateUser.setEmail(user.getEmail());
    updateUser.setPhone(user.getPhone());
    updateUser.setQuestion(user.getQuestion());
    updateUser.setAnswer(user.getAnswer());

    int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
    if (updateCount > 0) {
      ServerResponse.createBySuccess("更新个人信息成功！", updateUser);
    }
    return ServerResponse.createByErrorMessage("更新个人信息失败！");
  }

  @Override
  public ServerResponse<User> getInformation(Integer userId) {
    User user = userMapper.selectByPrimaryKey(userId);
    if (user == null) {
      return ServerResponse.createByErrorMessage("不存在该用户");
    }
    user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);//不太明白

    return ServerResponse.createBySuccess(user);
  }

  @Override
  public ServerResponse checkAdminRole(User user) {
    if (user != null && user.getRole() == Role.ROLE_ADMIN){
    return ServerResponse.createBySuccess();
    }
    return ServerResponse.createByError();
  }
}
