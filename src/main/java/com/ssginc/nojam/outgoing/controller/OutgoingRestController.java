package com.ssginc.nojam.outgoing.controller;

import com.ssginc.nojam.order.mapper.OrderMapperQ;
import com.ssginc.nojam.order.vo.OrderVOQ;
import com.ssginc.nojam.outgoing.mapper.OutgoingMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author Queue-ri
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("outgoing")
public class OutgoingRestController {
    private final OutgoingMapper outgoingMapper;
    private final OrderMapperQ orderMapperQ;

    @GetMapping("/pending/confirm")
    public void confirmOrder(@RequestParam("oid") Long orderId, HttpSession session) {
        outgoingMapper.confirm(orderId, LocalDateTime.now());
        OrderVOQ order = orderMapperQ.findOrderById(orderId);
        // 신규 출고 내역 추가
        outgoingMapper.insertNewOutgoing(orderId, order.getQuantity(), (String)session.getAttribute("userId"), LocalDateTime.now());
        // 본사 재고 차감
        outgoingMapper.decHeadStock(order.getQuantity(), order.getItemId());
    }

    @GetMapping("/pending/reject")
    public void rejectOrder(@RequestParam("oid") Long orderId) {
        outgoingMapper.reject(orderId);
    }

    @GetMapping("/indelivery/done")
    public void doneOutgoing(@RequestParam("oid") Long orderId) {
        OrderVOQ order = orderMapperQ.findOrderById(orderId);
        // 출고 완료 처리 - 다중 SQL 실행
        outgoingMapper.markDone(orderId, order.getBranchId() + "_" + order.getOrderId(), order.getItemId(), order.getBranchId(), order.getQuantity(), LocalDateTime.now());
        // 발주 요청했던 지점의 해당 상품 재고 증가
        outgoingMapper.incBranchStock(order.getQuantity(), order.getItemId());
    }
}
