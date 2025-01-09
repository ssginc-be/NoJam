package com.ssginc.nojam.stock.vo;

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
public class BranchStockVO {
    private String stockId;
    private Long itemId;
    private String branchId;
    private Integer stock;
    private LocalDateTime lastModifiedAt;
}
