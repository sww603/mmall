package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.Const.cart;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sww_6 on 2018/10/8.
 */
@Service
public class CartServiceImpl implements ICartService {

  @Autowired
  private CartMapper cartMapper;
  @Autowired
  private ProductMapper productMapper;

  @Override
  public ServerResponse add(Integer userId, Integer productId, Integer count) {
    Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
    if (cart == null) {
      Cart cartItem = new Cart();
      cartItem.setChecked(Const.cart.CHENCKED);
      cartItem.setQuantity(count);
      cartItem.setUserId(userId);
      cartItem.setProductId(productId);
      cartMapper.insert(cartItem);
    } else {
      count = cart.getQuantity() + count;
      cart.setQuantity(count);
      cartMapper.updateByPrimaryKeySelective(cart);
    }
    return this.list(userId);
  }

  @Override
  public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
    if (productId == null || count == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
          ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }
    Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
    if (null != cart) {
      cart.setQuantity(count);
    }
    cartMapper.updateByPrimaryKeySelective(cart);
    CartVo cartVoLimit = this.getCartVoLimit(userId);
    return ServerResponse.createBySuccess(cartVoLimit);
  }

  @Override
  public ServerResponse list(Integer userId) {
    CartVo cartVo = this.getCartVoLimit(userId);
    return ServerResponse.createBySuccess(cartVo);
  }

  private CartVo getCartVoLimit(Integer userId) {
    CartVo cartVo = new CartVo();
    List<Cart> cartLists = cartMapper.selectCartByUserId(userId);

    List<CartProductVo> cartProductVos = Lists.newArrayList();
    BigDecimal cartTotalPrice = new BigDecimal("0");

    if (CollectionUtils.isNotEmpty(cartLists)) {
      for (Cart cartItem : cartLists) {
        CartProductVo cartProductVo = new CartProductVo();
        cartProductVo.setId(cartItem.getId());
        cartProductVo.setUserId(userId);
        cartProductVo.setProductChecked(cartItem.getChecked());
        cartProductVo.setProductId(cartItem.getProductId());
        cartProductVo.setQuantity(cartItem.getQuantity());
        Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());

        if (product != null) {
          cartProductVo.setProductMainImage(product.getMainImage());
          cartProductVo.setProductName(product.getName());
          cartProductVo.setProductPrice(product.getPrice());
          cartProductVo.setProductStatus(product.getStatus());
          cartProductVo.setProductStock(product.getStock());
          cartProductVo.setProductSubTitle(product.getSubImages());

          int buyLimitCount = 0;
          //判断库存
          if (product.getStock() >= cartItem.getQuantity()) {
            buyLimitCount = cartItem.getQuantity();
            cartProductVo.setLimitQuantity(cart.LIMIT_NUM_SUCESS);
          } else {
            buyLimitCount = cartItem.getQuantity();
            cartProductVo.setLimitQuantity(cart.LIMIT_NUM_FATL);
            //购物车中更新有效库存
            Cart cartForQuantity = new Cart();
            cartForQuantity.setId(cartItem.getId());
            cartForQuantity.setQuantity(buyLimitCount);
            cartMapper.updateByPrimaryKeySelective(cartForQuantity);
          }
          cartProductVo.setQuantity(buyLimitCount);
          cartProductVo.setProductTotalPrice(BigDecimalUtil
              .mul(product.getPrice().doubleValue(), cartProductVo.getQuantity().doubleValue()));
          cartProductVo.setProductChecked(cartItem.getChecked());
        }

        if (cartItem.getChecked() == cart.CHENCKED) {
          cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),
              cartProductVo.getProductTotalPrice().doubleValue());
        }
        cartProductVos.add(cartProductVo);
      }
    }
    cartVo.setCartTotalPrice(cartTotalPrice);
    cartVo.setCartProductVos(cartProductVos);
    cartVo.setAllChecked(this.getAllCheckedStatus(userId));
    cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
    return cartVo;
  }

  private boolean getAllCheckedStatus(Integer userId) {
    if (userId == null) {
      return false;
    }
    return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;

  }
}
