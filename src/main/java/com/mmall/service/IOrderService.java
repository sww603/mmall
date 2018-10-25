package com.mmall.service;

import com.mmall.common.ServerResponse;
import java.util.Map;

/**
 * Created by sww_6 on 2018/10/21.
 */
public interface IOrderService {

  ServerResponse pay(Long orderId, Integer userId, String path);

  ServerResponse AliCallBack(Map<String, String> params);

  ServerResponse createOrder(Integer userId,Integer shippingId);

}
