package com.ssginc.nojam.order.mapper;

import com.ssginc.nojam.order.vo.ItemVO;
import com.ssginc.nojam.order.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<ItemVO> getAllItems();

    List<OrderVO> getCurrentOrders();

    int insertOrder(OrderVO order);

    List<OrderVO> getOrders();

    int countAllOrders();

    List<OrderVO> getFilteredOrders(@Param("category") String category,
                                    @Param("value") String value,
                                    @Param("startIdx") String startIdx);

    int countFilteredOrders(@Param("category") String category,
                            @Param("value") String value);

    // 출고대기 상태 발주 삭제 (취소)
    int cancelOrders(@Param("orderIds") List<String> orderIds);

}