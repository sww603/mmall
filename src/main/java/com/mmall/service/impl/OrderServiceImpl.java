package com.mmall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.Const.AlipayCallback;
import com.mmall.common.Const.OrderStatusEnum;
import com.mmall.common.Const.ProductStatusEnum;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.OrderItemMapper;
import com.mmall.dao.OrderMapper;
import com.mmall.dao.PayInfoMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Order;
import com.mmall.pojo.OrderItem;
import com.mmall.pojo.PayInfo;
import com.mmall.pojo.Product;
import com.mmall.service.IOrderService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.FTPUtil;
import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sww_6 on 2018/10/21.
 */
@Service("iOrderService")
@Slf4j
public class OrderServiceImpl implements IOrderService {

  @Autowired
  private OrderMapper orderMapper;

  @Autowired
  private OrderItemMapper orderItemMapper;
  @Autowired
  private PayInfoMapper payInfoMapper;
  @Autowired
  private CartMapper cartMapper;
  @Autowired
  private ProductMapper productMapper;

  @Override
  public ServerResponse pay(Long orderId, Integer userId, String path) {

    Map<String, String> result = Maps.newHashMap();
    Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderId);
    if (order == null) {
      return ServerResponse.createByErrorMessage("用户没有该订单");
    }
    result.put("orderNo", String.valueOf(order.getOrderNo()));

    // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
    // 需保证商户系统端不能重复，建议通过数据库sequence生成，
    String outTradeNo = order.getOrderNo().toString();

    // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
    String subject = new StringBuilder().append("happymall扫码支付，订单号：").append(outTradeNo).toString();

    // (必填) 订单总金额，单位为元，不能超过1亿元
    // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
    String totalAmount = order.getPayment().toString();

    // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
    // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
    String undiscountableAmount = "0";

    // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
    // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
    String sellerId = "";

    // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
    String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append("元")
        .toString();

    // 商户操作员编号，添加此参数可以为商户操作员做销售统计
    String operatorId = "test_operator_id";

    // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
    String storeId = "test_store_id";

    // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
    ExtendParams extendParams = new ExtendParams();
    extendParams.setSysServiceProviderId("2088100200300400500");

    // 支付超时，定义为120分钟
    String timeoutExpress = "120m";

    // 商品明细列表，需填写购买商品详细信息，
    List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
    // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
    // 创建好一个商品后添加至商品明细列表
    List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderId, userId);
    for (OrderItem orderItem : orderItemList) {
      GoodsDetail goods1 = GoodsDetail
          .newInstance(orderItem.getOrderNo().toString(), orderItem.getProductName(),
              BigDecimalUtil
                  .mul(orderItem.getCurrentUnitPrice().doubleValue(), new Double(100).doubleValue())
                  .longValue(),
              orderItem.getQuantity());
      goodsDetailList.add(goods1);
    }
    // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
    GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
    goodsDetailList.add(goods2);

    // 创建扫码支付请求builder，设置请求参数
    AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
        .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
        .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
        .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
        .setTimeoutExpress(timeoutExpress)
        .setNotifyUrl(
            PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
        .setGoodsDetailList(goodsDetailList);

    // 支付宝当面付2.0服务
    /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
     *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
     */
    Configs.init("zfbinfo.properties");

    /** 使用Configs提供的默认参数
     *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
     */
    AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    AlipayF2FPrecreateResult results = tradeService.tradePrecreate(builder);
    switch (results.getTradeStatus()) {
      case SUCCESS:
        log.info("支付宝预下单成功: )");

        AlipayTradePrecreateResponse response = results.getResponse();
        dumpResponse(response);

        File folder = new File(path);
        if (!folder.exists()) {
          folder.setWritable(true);
          folder.mkdir();
        }
        String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
        String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
        ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

        File targetfile = new File(path, qrFileName);
        try {
          FTPUtil.uploadFile(Lists.<File>newArrayList(targetfile));
        } catch (IOException e) {
          log.error("上传二维码异常！");
          e.printStackTrace();
        }

        log.info("qrPath:" + qrPath);

        String qrUrl = PropertiesUtil.getProperty("alipay.callback.url" + targetfile.getName());
        result.put("qrUrl", qrUrl);
        return ServerResponse.createBySuccess(result);
      case FAILED:
        log.error("支付宝预下单失败!!!");
        return ServerResponse.createBySuccessMessage("支付宝预下单失败!!!");
      case UNKNOWN:
        log.error("系统异常，预下单状态未知!!!");
        return ServerResponse.createBySuccessMessage("系统异常，预下单状态未知!!!");
      default:
        log.error("不支持的交易状态，交易返回异常!!!");
        return ServerResponse.createBySuccessMessage("不支持的交易状态，交易返回异常!!!");
    }
  }

  @Override
  public ServerResponse AliCallBack(Map<String, String> params) {
    Long orderNo = Long.parseLong(params.get("out_trade_no"));
    String tradeNo = params.get("trade_no");
    String tradeStatus = params.get("trade_status");
    Order order = orderMapper.selectByOrderNo(orderNo);
    if (order == null) {
      return ServerResponse.createByErrorMessage("非快乐慕商城的订单,回调忽略");
    }
    if (order.getStatus() >= OrderStatusEnum.PAID.getCode()) {
      return ServerResponse.createByErrorMessage("支付宝重复调用");
    }
    if (AlipayCallback.RESPONSE_SUCCESS.equals(tradeStatus)) {
      order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
      order.setStatus(OrderStatusEnum.PAID.getCode());
      orderMapper.updateByPrimaryKeySelective(order);
    }
    PayInfo payInfo = new PayInfo();
    payInfo.setUserId(order.getUserId());
    payInfo.setOrderNo(order.getOrderNo());
    payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
    payInfo.setPlatformNumber(tradeNo);
    payInfo.setPlatformStatus(tradeStatus);
    payInfoMapper.insert(payInfo);
    return ServerResponse.createBySuccess();
  }

  @Override
  public ServerResponse createOrder(Integer userId, Integer shippingId) {
    List<Cart> cartList = cartMapper.selectCartByUserId(userId);

    return null;
  }

  private ServerResponse getCartOrderItem(Integer userId, List<Cart> cartList) {
    List<OrderItem> orderItemList = Lists.newArrayList();
    if (CollectionUtils.isEmpty(orderItemList)) {
      return ServerResponse.createByErrorMessage("购物车为空");
    }
    //校验购物车的数据,包括产品的状态和数量
    for (Cart catItem : cartList) {
      OrderItem orderItem = new OrderItem();
      Product product = productMapper.selectByPrimaryKey(orderItem.getId());
      if (ProductStatusEnum.ON_SALE.getCode() != product.getStatus()) {
        return ServerResponse.createByErrorMessage("产品" + product.getName() + "不是在线售卖状态");
      }
      //校验库存
      if (catItem.getQuantity() > product.getStock()) {
        return ServerResponse.createByErrorMessage("产品" + product.getName() + "库存不足");
      }
      orderItem.setUserId(userId);
      orderItem.setProductId(product.getId());
      orderItem.setProductName(product.getName());
      orderItem.setProductImage(product.getMainImage());
      orderItem.setCurrentUnitPrice(product.getPrice());
      orderItem.setQuantity(catItem.getQuantity());
      orderItem.setTotalPrice(BigDecimalUtil
          .mul(product.getPrice().doubleValue(), catItem.getQuantity().doubleValue()));
      orderItemList.add(orderItem);
    }
    return ServerResponse.createBySuccess(orderItemList);
  }
  // 简单打印应答

  private void dumpResponse(AlipayResponse response) {
    if (response != null) {
      log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
      if (StringUtils.isNotEmpty(response.getSubCode())) {
        log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
            response.getSubMsg()));
      }
      log.info("body:" + response.getBody());
    }
  }
}
