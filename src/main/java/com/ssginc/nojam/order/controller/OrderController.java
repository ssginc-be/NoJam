package com.ssginc.nojam.order.controller;

import com.ssginc.nojam.order.dto.OrderDTO;
import com.ssginc.nojam.order.vo.ItemVO;
import com.ssginc.nojam.order.dto.OrderListResponseDto;
import com.ssginc.nojam.order.service.OrderService;
import com.ssginc.nojam.order.vo.OrderVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * 발주 등록 페이지 이동
     */
    @GetMapping("/items/view")
    public String showOrderPage() {

        return "order/register";
    }

    /**
     * 아이템 목록 조회(API 예시)
     */
    @GetMapping("/items")
    public ResponseEntity<List<ItemVO>> getItems() {
        return ResponseEntity.ok(orderService.getAllItems());
    }

    /**
     * 발주 등록 페이지 이동
     */
    /*@GetMapping("/register")
    public String showOrderPage(Model model, HttpSession session) {
        String branchId = "B00001";

        model.addAttribute("item", orderService.getAllItems());
        model.addAttribute("order", orderService.getCurrentOrders());
        return "order/register";
    }*/

    /**
     * 발주 등록 처리
     */





    @PostMapping("/register")
    public String registerOrders(@RequestBody List<OrderDTO> orders,
                                 RedirectAttributes redirectAttributes, HttpSession session) {

        List<OrderVO> orderVOs = new ArrayList<>();

        for (OrderDTO order : orders) {
            OrderVO orderVO = new OrderVO();
            orderVO.setBranchId(session.getAttribute("branchId").toString());
            orderVO.setItemId(order.getItemId());
            orderVO.setQuantity(order.getQuantity());
            orderVOs.add(orderVO);
            System.out.println("Item ID: " + order.getItemId());
            System.out.println("Quantity: " + order.getQuantity());
        }


        orderService.registerOrders(orderVOs);
        redirectAttributes.addFlashAttribute("message", "발주가 성공적으로 등록되었습니다.");
        return "redirect:/order/history/view";
    }

    @GetMapping("/history/view")
    public String showOrderHistoryPage() {
        return "order/history"; // HTML 템플릿 경로
    }

    /**
     * 발주 내역 조회(히스토리)
     * - 검색/페이징을 위해 파라미터 사용
     */
    @GetMapping("/history/orderhistory")
    public ResponseEntity<OrderListResponseDto> getOrders() {

        List<OrderVO> result = orderService.getOrders();
        int totalCount = orderService.countAllOrders();
        int totalPages = (int) Math.ceil((double) totalCount / 10);

        /*Map<String, Object> response = new HashMap<>();
        response.put("items", result);
        response.put("totalCount", result);
        response.put("totalPages", result);*/

        OrderListResponseDto response = new OrderListResponseDto(result, totalCount, totalPages);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cancel/view")
    public String showOrderCancelPage(){
        return "/order/cancelorder";
    }

    /**
     * 발주 취소 (체크박스에서 선택된 orderId 리스트를 받아옴)
     * → status='출고대기' 주문을 실제 DB에서 DELETE
     */
    @PostMapping("/history/cancel")
    @ResponseBody
    public ResponseEntity<String> cancelOrders(@RequestBody List<String> orderIds) {
        int updatedCount = orderService.cancelOrders(orderIds);
        if (updatedCount > 0) {
            return ResponseEntity.ok(updatedCount + "건의 발주가 취소(삭제)되었습니다.");
        } else {
            return ResponseEntity.ok("취소할 수 있는 발주가 없거나 이미 다른 상태입니다.");
        }
    }


}
