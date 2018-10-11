package com.mmall.vo;

import java.math.BigDecimal;
import lombok.Data;

/**
 * Created by sww_6 on 2018/10/11.
 */
@Data
public class CartProductVo {

  private Integer id;

  private Integer userId;

  private Integer productId;

  private Integer quantity;

  private String productName;

  private String productSubTitle;

  private String productMainImage;

  private BigDecimal productPrice;

  private Integer productStatus;

  private BigDecimal productTotalPrice;

  private Integer productStock;

  private Integer productChecked;

  private String limitQuantity;
}
