package com.ssginc.nojam.outgoing.vo;

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
public class OutgoingVO {
    private Long outgoingId;
    private Long orderId;
    private String status;
    private Integer quantity;
    private LocalDateTime outStartTime;
    private LocalDateTime outEndTime;
}
