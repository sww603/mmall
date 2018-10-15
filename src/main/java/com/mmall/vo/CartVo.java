package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 * Created by sww_6 on 2018/10/11.
 */
@Data
public class CartVo {

  private List<CartProductVo> cartProductVos;

  private BigDecimal cartTotalPrice;

  private Boolean allChecked;

  private String imageHost;

}

