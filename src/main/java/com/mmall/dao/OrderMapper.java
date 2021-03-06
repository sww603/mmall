package com.mmall.dao;

import com.mmall.pojo.Cart;
import com.mmall.pojo.Order;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {

  int deleteByPrimaryKey(Integer id);

  int insert(Order record);

  int insertSelective(Order record);

  Order selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(Order record);

  int updateByPrimaryKey(Order record);

  Order selectByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderId") Long orderId);

  Order selectByOrderNo(Long orderNo);
}