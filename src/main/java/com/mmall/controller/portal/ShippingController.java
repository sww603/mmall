package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by sww_6 on 2018/10/14.
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

  @Autowired
  private IShippingService shippingService;

  @RequestMapping("/add")
  private ServerResponse add(HttpSession session, Shipping shipping) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (null != user) {
      return ServerResponse
          .createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
    }
    return shippingService.add(user.getId(), shipping);
  }
}
