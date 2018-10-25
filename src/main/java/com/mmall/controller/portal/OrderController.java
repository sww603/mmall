package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.Const.AlipayCallback;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by sww_6 on 2018/10/21.
 */
@Controller
@RequestMapping("/order/")
@Slf4j
public class OrderController {

  @Autowired
  private IOrderService orderService;
  @RequestMapping("create.do")
  @ResponseBody
  public ServerResponse create(HttpSession session, Integer shippingId){
    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if(user ==null){
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
    }
    return orderService.createOrder(user.getId(),shippingId);
  }
  @RequestMapping("pay.do")
  @ResponseBody
  public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request) {
    User attribute = (User) session.getAttribute(Const.CURRENT_USER);
    if (null != attribute) {
      return ServerResponse
          .createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
    }
    String path = request.getSession().getServletContext().getRealPath("uploud");
    return orderService.pay(orderNo, attribute.getId(), path);
  }

  @RequestMapping("alipay_callback.do")
  @ResponseBody
  private Object alipayCallback(HttpServletRequest request) {
    Map<String, String> params = Maps.newHashMap();

    Map requestParams = request.getParameterMap();
    for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
      String name = (String) iter.next();
      String[] values = (String[]) requestParams.get(name);
      String valueStr = "";
      for (int i = 0; i < values.length; i++) {
        valueStr = (i == values.length) ? valueStr + values[i] : values + values[i] + ",";
      }
      params.put(name, valueStr);
    }
    log.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"),
        params.toString());

    params.remove("sign_type");
    try {
      boolean alipayRSACheckedV2 = AlipaySignature
          .rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
      if (!alipayRSACheckedV2) {
        return ServerResponse.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
      }
    } catch (AlipayApiException e) {
      e.printStackTrace();
      log.error("支付宝回调异常！");
    }

    // TODO:验证各种数据
    ServerResponse Response = orderService.AliCallBack(params);
    if (Response.isSuccess()) {
      return AlipayCallback.RESPONSE_SUCCESS;
    }
    return AlipayCallback.RESPONSE_FAILED;
  }
}
