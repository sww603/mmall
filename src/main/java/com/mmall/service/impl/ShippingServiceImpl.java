package com.mmall.service.impl;

import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sww_6 on 2018/10/14.
 */
@Service
public class ShippingServiceImpl implements IShippingService {

  @Autowired
  private ShippingMapper shippingMapper;

  @Override
  public ServerResponse add(Integer userId, Shipping shipping) {
    shipping.setUserId(userId);
    int count = shippingMapper.insert(shipping);
    if (count > 0) {
      Map result = Maps.newHashMap();
      result.put("shippingId", shipping.getId());
      return ServerResponse.createBySuccess("新建地址成功！", result);
    }
    return ServerResponse.createByErrorMessage("新建地址失败!");
  }
}
