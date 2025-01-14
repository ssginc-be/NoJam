package com.ssginc.nojam.order.mapper;

import com.ssginc.nojam.order.vo.OrderVOQ;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Queue-ri
 */

@Mapper
public interface OrderMapperQ {

    OrderVOQ findOrderById(@Param("orderId") Long orderId);
}
