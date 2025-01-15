package com.ssginc.nojam.outgoing.dto;

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
public class OutgoingViewDTO {
    // ** 주의 **
    // 출고 테이블 아니고 발주 테이블에서 가져오는 것임
    // 출고대기 상태에선 출고 테이블에 들어가는 데이터가 없음
    private Long outgoingId;
    private Long orderId;
    private Long itemId;
    private String name; // item name. join
    private String branchName; // join
    private Integer price; // join
    private Integer quantity;
    private Integer stock; // head stock. join
    private String status;
    private LocalDateTime orderTime;
    private LocalDateTime outStartTime;
    private LocalDateTime outEndTime;
}
