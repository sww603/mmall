package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * Created by sww_6 on 2018/10/14.
 */
public interface IShippingService {

  ServerResponse add(Integer userId, Shipping shipping);
}
