package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by sww_6 on 2018/7/4.
 */
public interface IUserService {

  ServerResponse<User> login(String username, String password);

  ServerResponse<String> register(User user);

  ServerResponse<String> checkValid(String str, String type);

  ServerResponse<String> selectQuestion(String userName);

  ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

  ServerResponse<User> updateInformation(User user);

  ServerResponse<User> getInformation(Integer userId);

  ServerResponse checkAdminRole(User user);
}
