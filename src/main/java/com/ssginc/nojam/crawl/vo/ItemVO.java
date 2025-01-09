package com.ssginc.nojam.crawl.vo;

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
public class ItemVO {
    private Long itemId;
    private String name;
    private String category1;
    private String category2;
    private int price;
    private LocalDateTime lastModifiedAt;
}
