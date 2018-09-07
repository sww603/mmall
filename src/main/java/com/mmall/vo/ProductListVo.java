package com.mmall.vo;

import java.math.BigDecimal;
import lombok.Data;

/**
 * Created by sww_6 on 2018/9/7.
 */
@Data
public class ProductListVo {

  private Integer id;

  private Integer categoryId;

  private String name;

  private String subtitle;

  private String mainImage;

  private BigDecimal price;

  private Integer status;

  private String imageHost;
}
