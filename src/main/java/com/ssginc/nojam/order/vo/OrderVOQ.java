package com.ssginc.nojam.order.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Queue-ri
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderVOQ {
    private Long orderId;
    private Long itemId;
    private String branchId;
    private String status;
    private Integer quantity;
    private LocalDateTime orderTime;
    private LocalDateTime outStartTime;
    private LocalDateTime inEndTime;
}
