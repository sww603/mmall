package com.mmall.vo;

import java.math.BigDecimal;
import lombok.Data;

/**
 * Created by sww_6 on 2018/9/6.
 */
@Data
public class ProductDetailVo {

  private Integer id;
  private Integer productId;
  private String name;
  private String subTitle;
  private String mainImage;
  private String subImages;
  private String details;
  private BigDecimal price;
  private Integer stock;
  private Integer status;

  private String createTime;
  private String updateTime;

  private String imageHost;
  private Integer parentCategoryId;


}
