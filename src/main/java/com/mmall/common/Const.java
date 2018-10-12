package com.mmall.common;

/**
 * Created by geely
 */
public class Const {

  public static final String CURRENT_USER = "currentUser";
  public static final String EMAIL = "email";
  public static final String USERNAME = "userName";

  public interface Role {

    int ROLE_CUSTOMER = 0; //普通用户
    int ROLE_ADMIN = 1;//管理员
  }

  public interface cart {

    int CHENCKED = 1;//被选中
    int UN_CHENCKED = 0;//未被选中

    String LIMIT_NUM_FATL = "LIMIT_NUM_FATL";
    String LIMIT_NUM_SUCESS = "LIMIT_NUM_SUCESS";
  }
}
