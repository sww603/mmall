package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * Created by sww_6 on 2018/10/14.
 */
public interface IShippingService {

  ServerResponse add(Integer userId, Shipping shipping);

  ServerResponse delect(Integer userId, Integer shippingId);

  ServerResponse update(Integer userId, Shipping shipping);

  ServerResponse<Shipping> select(Integer userId, Integer shippingId);

  ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
}
