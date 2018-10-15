package com.mmall.common;

import com.google.common.collect.Sets;
import java.util.Set;

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

  public enum ProductStatusEnum{
    ON_SALE(1,"在线");
    private String value;
    private int code;
    ProductStatusEnum(int code,String value){
      this.code = code;
      this.value = value;
    }
    public String getValue() {
      return value;
    }

    public int getCode() {
      return code;
    }
  }
  public interface ProductListOrderBy{
    Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
  }
}
