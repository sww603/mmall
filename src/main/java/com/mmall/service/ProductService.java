package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * Created by sww_6 on 2018/9/6.
 */
public interface ProductService {

  ServerResponse saveOrUpdateProduct(Product product);

  ServerResponse<String> setSaleStatus(Integer productId, Integer status);

  ServerResponse<ProductDetailVo> managerProductDetails(Integer productId);

  ServerResponse<PageInfo> getProductList(int pageNum,int pageSize);

  ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum,
      Integer pageSize);

  ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

  ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);

}
