package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.pojo.Cart;
import com.mmall.service.ICartService;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sww_6 on 2018/10/8.
 */
@Service
public class CartServiceImpl implements ICartService {

  @Autowired
  private CartMapper cartMapper;

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
    return null;
  }

  private CartVo cart(Integer userId) {
    CartVo cartVo = new CartVo();
    List<Cart> carts = cartMapper.selectCartByUserId(userId);

    List<CartProductVo> cartProductVos = Lists.newArrayList();
    BigDecimal cartTotalPrice = new BigDecimal("0");
    return cartVo;
  }
}
