package com.mmall.util;


import java.math.BigDecimal;

/**
 * Created by sww_6 on 2018/10/11.
 */
public class BigDecimalUtil {

  private BigDecimalUtil() {
  }

  public static BigDecimal add(Double d1, Double d2) {
    BigDecimal b1 = new BigDecimal(Double.toString(d1));
    BigDecimal b2 = new BigDecimal(Double.toString(d2));
    return b1.add(b2);
  }

  public static BigDecimal sub(Double d1, Double d2) {
    BigDecimal b1 = new BigDecimal(Double.toString(d1));
    BigDecimal b2 = new BigDecimal(Double.toString(d2));
    return b1.subtract(b2);
  }

  public static BigDecimal mul(Double d1, Double d2) {
    BigDecimal b1 = new BigDecimal(Double.toString(d1));
    BigDecimal b2 = new BigDecimal(Double.toString(d2));
    return b1.multiply(b2);
  }

  public static BigDecimal div(Double d1, Double d2) {
    BigDecimal b1 = new BigDecimal(Double.toString(d1));
    BigDecimal b2 = new BigDecimal(Double.toString(d2));
    return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);//四舍五入保留两位小数
  }
}
