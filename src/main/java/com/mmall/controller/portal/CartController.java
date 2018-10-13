package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import java.math.BigDecimal;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by sww_6 on 2018/10/8.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

  @Autowired
  private ICartService iCartService;

  @RequestMapping("add.do")
  @ResponseBody
  private ServerResponse add(HttpSession session, Integer userId, Integer productId,
      Integer count) {
    User attribute = (User) session.getAttribute(Const.CURRENT_USER);
    if (null != attribute) {
      return ServerResponse
          .createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
    }
    return iCartService.add(userId, productId, count);
  }

  @RequestMapping("update.do")
  @ResponseBody
  private ServerResponse update(HttpSession session, Integer userId, Integer productId,
      Integer count) {
    User attribute = (User) session.getAttribute(Const.CURRENT_USER);
    if (null != attribute) {
      return ServerResponse
          .createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
    }
    return iCartService.update(userId, productId, count);
  }

  @RequestMapping("delect.do")
  @ResponseBody
  private ServerResponse delect(HttpSession session, Integer userId, String productIds,
      Integer count) {
    User attribute = (User) session.getAttribute(Const.CURRENT_USER);
    if (null != attribute) {
      return ServerResponse
          .createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
    }
    return iCartService.delectProduct(userId, productIds);
  }
}
