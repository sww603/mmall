package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

  int deleteByPrimaryKey(Integer id);

  int insert(User record);

  int insertSelective(User record);

  User selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(User record);

  int updateByPrimaryKey(User record);

  int checkUsername(String username);

  int checkEmail(String email);

  User selectLogin(@Param("username") String username, @Param("password") String password);

  /**
   *根据用户名搜索问题
   */
  String selectQueationByUsername(String username);

  int checkPassword (@Param("password") String password,@Param("userId") Integer userId);

  int checkEmailByUserId(@Param(value="email")String email,@Param(value="userId")Integer userId);
}