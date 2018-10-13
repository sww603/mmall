package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;
import javax.swing.Spring;

/**
 * Created by sww_6 on 2018/10/8.
 */
public interface ICartService {

  public ServerResponse add(Integer userId, Integer productId, Integer count);

  public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

  ServerResponse<CartVo> delectProduct(Integer userId, String productIds);

  public ServerResponse list(Integer userId);
}
