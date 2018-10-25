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
  public static String getCurrentUser() {
    return CURRENT_USER;
  }

  public static String getEMAIL() {
    return EMAIL;
  }

  public enum OrderStatusEnum{
    CANCELED(0,"已取消"),
    NO_PAY(10,"未支付"),
    PAID(20,"已付款"),
    SHIPPED(40,"已发货"),
    ORDER_SUCCESS(50,"订单完成"),
    ORDER_CLOSE(60,"订单关闭");
    OrderStatusEnum(int code,String value){
      this.code = code;
      this.value = value;
    }
    private String value;
    private int code;

    public java.lang.String getValue() {
      return value;
    }

    public void setValue(java.lang.String value) {
      this.value = value;
    }

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }
  }

  public enum PayPlatformEnum{
    ALIPAY(1,"支付宝");

    PayPlatformEnum(int code,String value){
      this.code = code;
      this.value = value;
    }
    private String value;
    private int code;

    public String getValue() {
      return value;
    }

    public int getCode() {
      return code;
    }
  }

  public interface  AlipayCallback{
    String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

    String RESPONSE_SUCCESS = "success";
    String RESPONSE_FAILED = "failed";
  }

  public enum ProductStatusEnum {
    ON_SALE(1, "在线");
    private String value;
    private int code;

    ProductStatusEnum(int code, String value) {
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

  public interface ProductListOrderBy {

    Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
  }
}
