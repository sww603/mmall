package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by sww_6 on 2018/10/21.
 */
@Controller
@RequestMapping("/order/")
public class OrderController {
    @Autowired
    private IOrderService orderService;

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
}
