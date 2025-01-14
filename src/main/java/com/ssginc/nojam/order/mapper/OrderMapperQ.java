package com.ssginc.nojam.order.mapper;

import com.ssginc.nojam.crawl.vo.ItemVO;
import com.ssginc.nojam.order.vo.OrderVOQ;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Queue-ri
 */

@Mapper
public interface OrderMapperQ {

    OrderVOQ findOrderById(@Param("orderId") Long orderId);
    int insertOrders(@Param("od") List<OrderVOQ> orderList, @Param("branchId") String branchId, @Param("orderTime") LocalDateTime orderTime);
    List<ItemVO> getItemsByItemIdList(@Param("idList") List<Long> idList);
}
