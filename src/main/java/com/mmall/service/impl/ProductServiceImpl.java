package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import java.util.ArrayList;
import java.util.List;
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
  @Autowired
  private CategoryMapper categoryMapper;

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

  @Override
  public ServerResponse<ProductDetailVo> managerProductDetails(Integer productId) {
    if (productId == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
          ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }
    Product product = productMapper.selectByPrimaryKey(productId);
    if (product == null) {
      return ServerResponse.createByErrorMessage("商品已删除或已下架!");
    }
    ProductDetailVo productDetailVo = assembleProductDetailVo(product);
    return ServerResponse.createBySuccess(productDetailVo);
  }

  @Override
  public ServerResponse getProductList(int pageNum, int pageSize) {
    PageHelper.startPage(pageNum, pageSize);
    List<Product> products = productMapper.selectList();
    List<ProductListVo> productList = Lists.newArrayList();
    for (Product productItem : products) {
      ProductListVo productListVo = assembleProductListVo(productItem);
      productList.add(productListVo);
    }
    return ServerResponse.createBySuccess(productList);
  }

  private ProductListVo assembleProductListVo(Product product) {
    ProductListVo productListVo = new ProductListVo();
    productListVo.setId(product.getId());
    productListVo.setCategoryId(product.getCategoryId());
    productListVo.setName(product.getName());
    productListVo.setPrice(product.getPrice());
    productListVo.setStatus(product.getStatus());
    productListVo.setSubtitle(product.getSubtitle());
    productListVo.setMainImage(product.getMainImage());
    productListVo.setImageHost(
        PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
    return productListVo;
  }

  private ProductDetailVo assembleProductDetailVo(Product product) {
    ProductDetailVo productDetailVo = new ProductDetailVo();
    productDetailVo.setId(product.getId());
    productDetailVo.setSubTitle(product.getSubtitle());
    productDetailVo.setPrice(product.getPrice());
    productDetailVo.setMainImage(product.getMainImage());
    productDetailVo.setName(product.getName());
    productDetailVo.setStatus(product.getStatus());
    productDetailVo.setStock(product.getStock());

    productDetailVo.setImageHost(
        PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
    productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
    productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

    Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
    if (category == null) {
      productDetailVo.setParentCategoryId(0);
    } else {
      productDetailVo.setParentCategoryId(category.getParentId());
    }
    return productDetailVo;
  }
}
