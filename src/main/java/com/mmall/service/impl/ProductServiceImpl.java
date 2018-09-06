package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sww_6 on 2018/9/6.
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductMapper productMapper;

  @Override
  public ServerResponse saveOrUpdateProduct(Product product) {
    if (null != product) {
      if (StringUtils.isNotBlank(product.getSubImages())) {
        String[] subImageArray = product.getSubImages().split(",");
        if (subImageArray.length > 0) {
          product.setMainImage(subImageArray[0]);
        }
        if (product.getId() != null) {
          int rowCount = productMapper.updateByPrimaryKey(product);
          if (rowCount > 0) {
            ServerResponse.createBySuccessMessage("更新产品成功！");
          }
        } else {
          int rowCount = productMapper.insertSelective(product);
          if (rowCount > 0) {
            ServerResponse.createBySuccessMessage("新增产品成功！");
          }
        }
      }
    }
    return ServerResponse.createByErrorMessage("新增产品参数不正确！");
  }

  @Override
  public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
    if (productId == null || status == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
          ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }
    Product product = new Product();
    product.setId(productId);
    product.setStatus(status);
    int rowCount = productMapper.updateByPrimaryKeySelective(product);
    if (rowCount > 0) {
      return ServerResponse.createBySuccessMessage("更新产品成功！");
    }
    return ServerResponse.createByErrorMessage("更新产品失败！");
  }
}
