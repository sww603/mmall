package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * Created by sww_6 on 2018/9/6.
 */
public interface ProductService {

  ServerResponse saveOrUpdateProduct(Product product);

  ServerResponse<String> setSaleStatus(Integer productId, Integer status);
}
