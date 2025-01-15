package com.ssginc.nojam.order.service;


import com.ssginc.nojam.order.vo.ItemVO;
import com.ssginc.nojam.order.mapper.OrderMapper;
import com.ssginc.nojam.order.vo.OrderVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;

    public List<ItemVO> getAllItems() {
        return orderMapper.getAllItems();
    }

    public List<OrderVO> getCurrentOrders() {
        return orderMapper.getCurrentOrders();
    }

    public void registerOrders(List<OrderVO> orders) {
        for (OrderVO order : orders) {
            orderMapper.insertOrder(order);
        }
    }

    public List<OrderVO> getOrders() { //startIdx
        return orderMapper.getOrders();
    }

    public int countAllOrders() {
        return orderMapper.countAllOrders();
    }

    public List<OrderVO> getFilteredOrders(String category, String value, String startIdx) {
        return orderMapper.getFilteredOrders(category, value, startIdx);
    }

    public int countFilteredOrders(String category, String value) {
        return orderMapper.countFilteredOrders(category, value);
    }

    /**
     * 출고대기 상태 발주 취소(실제로 DB에서 삭제)
     */
    public int cancelOrders(List<String> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return 0;
        }
        return orderMapper.cancelOrders(orderIds);
    }
    //


}