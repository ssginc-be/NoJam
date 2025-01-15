package com.ssginc.nojam.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    private String orderId;             // 발주ID (PK)
    private int quantity;               // 수량
    private String status;              // 발주 상태 (출고대기, 출고중인, 출고완료, 입고완료, 취소 등)
    private LocalDateTime orderTime;
    private LocalDateTime outgoingStartTime;
    private LocalDateTime inEndTime;
    private Long itemId;                // 상품ID
    private String branchId;            // 지점ID

    // JOIN 결과용 (item 테이블)
    private String name;                // 상품명
    private String category1;           // 대분류
    private String category2;           // 소분류
}