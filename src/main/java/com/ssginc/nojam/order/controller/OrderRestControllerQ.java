package com.ssginc.nojam.order.controller;

import com.ssginc.nojam.crawl.vo.ItemVO;
import com.ssginc.nojam.order.mapper.OrderMapperQ;
import com.ssginc.nojam.order.vo.OrderVOQ;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Queue-ri
 */

@Slf4j
@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderRestControllerQ {
    private final OrderMapperQ orderMapperQ;

    @PostMapping("/save")
    public void saveOrderList(@RequestBody List<OrderVOQ> orderList, HttpSession session) {
        orderMapperQ.insertOrders(orderList, (String)session.getAttribute("branchId"), LocalDateTime.now());
    }

    @PostMapping("/fetch-info")
    public List<String> getItemNamesByItemIds(@RequestBody List<Long> idList) {
        List<ItemVO> voList = orderMapperQ.getItemsByItemIdList(idList);
        List<String> nameList = new ArrayList<>();
        for (ItemVO vo : voList) {
            nameList.add(vo.getName());
        }
        return nameList;
    }
}
