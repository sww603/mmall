package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * Created by sww_6 on 2018/10/21.
 */
public interface IOrderService {

    ServerResponse pay(Long orderId, Integer userId, String path);

}
