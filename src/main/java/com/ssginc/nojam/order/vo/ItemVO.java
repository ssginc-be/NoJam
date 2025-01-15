package com.ssginc.nojam.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVO {
    private int itemId;
    private String name;
    private String category1;
    private String category2;
    private int price;
    private LocalDateTime lastModifiedAt;

}
